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
import com.example.almacen.feature_activity.data.paging.ActivityHeadersPagingSource
import com.example.almacen.feature_activity.domain.model.ActivityHeader
import com.example.almacen.feature_activity.domain.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ActivityListViewModel @Inject constructor(
    private val repo: ActivityRepository
) : ViewModel() {

    var query by mutableStateOf("")
        private set

    private val _queryFlow = MutableStateFlow<String?>(null)

    private val pageSize = 10

    // 🔧 Normalizador: recorta, colapsa espacios, min. 3 chars
    private fun normalizeQuery(input: String?): String? {
        val q = input?.trim()?.replace(Regex("\\s+"), " ")
        return if (q.isNullOrBlank() || q.length < 3) null else q
    }

    val pagingFlow: Flow<PagingData<ActivityHeader>> =
        _queryFlow
            .map { normalizeQuery(it) }      // <- convierte “a”, “  ac  ”, etc. en null o texto limpio
            .debounce(200)                    // <- baja un poco el debounce
            .distinctUntilChanged()           // <- NO recrees Pager si no cambió realmente
            .flatMapLatest { nombreCliente ->
                Pager(
                    config = PagingConfig(
                        pageSize = pageSize,
                        initialLoadSize = pageSize * 2, // <- carga inicial más grande: sensación de rapidez
                        prefetchDistance = 5,            // <- pide antes de llegar al final
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = {
                        ActivityHeadersPagingSource(
                            repo = repo,
                            nombreCliente = nombreCliente, // null => lista por defecto
                            pageSize = pageSize
                        )
                    }
                ).flow
            }
            .cachedIn(viewModelScope)

    fun loadInitial() {
        // Emite null => listado por defecto (page=0, size=20)
        _queryFlow.value = null
    }

    fun onQueryChange(q: String) {
        query = q
        // Emitimos lo que escribió; normalizeQuery decidirá si es null (<3 chars)
        _queryFlow.value = q
    }
}
