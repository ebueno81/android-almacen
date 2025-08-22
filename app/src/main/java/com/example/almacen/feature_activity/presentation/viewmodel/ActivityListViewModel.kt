package com.example.almacen.feature_activity.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.almacen.core.network.ActivityEventsClient
import com.example.almacen.feature_activity.data.paging.ActivityHeadersPagingSource
import com.example.almacen.feature_activity.domain.model.ActivityHeader
import com.example.almacen.feature_activity.domain.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ActivityListViewModel @Inject constructor(
    private val repo: ActivityRepository
) : ViewModel() {

    private val _refreshFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val refreshFlow: SharedFlow<Unit> = _refreshFlow

    private var sseClient: ActivityEventsClient? = null

    var query by mutableStateOf("")
        private set

    private val _queryFlow = MutableStateFlow<String?>(null)
    private val pageSize = 10

    private fun normalizeQuery(input: String?): String? {
        val q = input?.trim()?.replace(Regex("\\s+"), " ")
        return if (q.isNullOrBlank() || q.length < 3) null else q
    }

    private val reloadTick = MutableStateFlow(0L)

    val pagingFlow: Flow<PagingData<ActivityHeader>> =
        combine(
            reloadTick,
            _queryFlow
                .map { normalizeQuery(it) }
                .debounce(200)
                .distinctUntilChanged()   // <- Solo a la query
        ) { _, nombreCliente -> nombreCliente } // <- cada tick emite aunque la query no cambie
            .flatMapLatest { nombreCliente ->
                Pager(
                    config = PagingConfig(
                        pageSize = pageSize,
                        initialLoadSize = pageSize * 2,
                        prefetchDistance = 5,
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = {
                        ActivityHeadersPagingSource(
                            repo = repo,
                            nombreCliente = nombreCliente,
                            pageSize = pageSize
                        )
                    }
                ).flow
            }
            .cachedIn(viewModelScope)

    fun forceReload() { reloadTick.value = System.currentTimeMillis() }

    fun loadInitial() { _queryFlow.value = null }

    fun onQueryChange(q: String) {
        query = q
        _queryFlow.value = q
    }

    fun startSse(baseUrl: String) {
        if (sseClient != null) return
        sseClient = ActivityEventsClient(
            baseUrl = baseUrl,
            onProcessed = { _, _ -> forceReload() },   // 👈 aquí
            onError = { /* log opcional */ }
        ).also { it.connect() }
    }

    /** Llama al backend para procesar la actividad */
    fun processActivity(
        id: Int,
        user: String = "android",
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val nuevoId = repo.processActivity(id, user).getOrThrow()
                withContext(Dispatchers.Main) { onSuccess(nuevoId) }
                forceReload() // 👈 refresca aunque el SSE tarde o no llegue
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError(e.message ?: "Error desconocido") }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        sseClient?.close()
        sseClient = null
    }
}
