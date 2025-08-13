package com.example.almacen.feature_activity.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.almacen.catalog.domain.model.Article
import com.example.almacen.catalog.domain.model.Client
import com.example.almacen.catalog.domain.model.Reason
import com.example.almacen.catalog.domain.model.Store
import com.example.almacen.catalog.domain.usecase.GetArticlesUseCase
import com.example.almacen.catalog.domain.usecase.GetReasonsUseCase
import com.example.almacen.catalog.domain.usecase.GetStoresUseCase
import com.example.almacen.catalog.domain.usecase.SearchClientsUseCase
import com.example.almacen.feature_activity.domain.model.Activity
import com.example.almacen.feature_activity.domain.repository.ActivityFormDetail
import com.example.almacen.feature_activity.domain.repository.ActivityRepository
import com.example.almacen.feature_activity.presentation.model.NewActivityDetailDraft
import com.example.almacen.feature_activity.presentation.model.NewActivityDraft
import com.example.almacen.feature_activity.presentation.state.NewActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewActivityViewModel @Inject constructor(
    private val repo: ActivityRepository,
    private val getStores: GetStoresUseCase,
    private val getReasons: GetReasonsUseCase,
    private val getArticles: GetArticlesUseCase,
    private val searchClients: SearchClientsUseCase
) : ViewModel() {

    // -------- UI STATE --------
    var stores by mutableStateOf<List<Store>>(emptyList())
        private set
    var reasons by mutableStateOf<List<Reason>>(emptyList())
        private set
    var articles by mutableStateOf<List<Article>>(emptyList())
        private set

    var selectedClient by mutableStateOf<Client?>(null)
        private set
    var selectedStore by mutableStateOf<Store?>(null)
        private set
    var selectedReason by mutableStateOf<Reason?>(null)
        private set

    var nroGuia by mutableStateOf("")
        private set
    var serieGuia by mutableStateOf("")
        private set
    var observaciones by mutableStateOf("")
        private set

    var detalles by mutableStateOf<List<NewActivityDetailDraft>>(emptyList())
        private set

    // -------- CLIENTES (Paging) --------
    private var clientQuery by mutableStateOf<String?>(null)
    val clientPagingFlow: Flow<PagingData<Client>> =
        snapshotFlow { clientQuery }
            .debounce(300)
            .map { q -> searchClients(q) }        // Pager<Int, Client>
            .flatMapLatest { pager -> pager.flow } // Flow<PagingData<Client>>
            .cachedIn(viewModelScope)

    // evitar choque con setter JVM: onClientQueryChange (NO setClientQuery)
    fun onClientQueryChange(q: String?) { clientQuery = q }

    fun selectClient(c: Client) { selectedClient = c }
    fun selectStore(s: Store) { selectedStore = s }
    fun selectReason(r: Reason) { selectedReason = r }

    fun onNroGuiaChange(v: String) { nroGuia = v }
    fun onSerieGuiaChange(v: String) { serieGuia = v }
    fun onObservacionesChange(v: String) { observaciones = v }

    fun addDetailRow() { detalles = detalles + NewActivityDetailDraft() }
    fun updateDetailRow(index: Int, newValue: NewActivityDetailDraft) {
        detalles = detalles.toMutableList().also { it[index] = newValue }
    }
    fun removeDetailRow(index: Int) {
        detalles = detalles.toMutableList().also { it.removeAt(index) }
    }

    fun buildDraft(): NewActivityDraft = NewActivityDraft(
        client = selectedClient,
        store = selectedStore,
        reason = selectedReason,
        nroGuia = nroGuia,
        serieGuia = serieGuia,
        observaciones = observaciones,
        detalles = detalles
    )

    // -------- STATE (Result) --------
    private val _state = MutableStateFlow(NewActivityState())
    val state: StateFlow<NewActivityState> = _state

    fun loadStaticLists() {
        viewModelScope.launch {
            try {
                stores = getStores()
                reasons = getReasons()
                articles = getArticles()
            } catch (_: Exception) {
                // opcional: _state con error de catálogos
            }
        }
    }

    // -------- CREATE --------
    private val currentUserId: String = "CURRENT_USER_ID" // TODO: usa tu Auth real
    fun createFromState() {
        val client = selectedClient ?: return setError("Selecciona cliente")
        val store = selectedStore ?: return setError("Selecciona almacén")
        val reason = selectedReason ?: return setError("Selecciona motivo")
        if (detalles.isEmpty()) return setError("Agrega al menos un detalle")

        val detallePairs = detalles.mapIndexed { idx, d ->
            val artId = d.articulo?.id?.toLong()
                ?: return setError("Falta artículo en el detalle ${idx + 1}")
            val peso = d.peso.toDoubleOrNull() ?: 0.0
            val cajas = d.cajas.toIntOrNull() ?: 0
            artId to ActivityFormDetail(
                lote = d.lote,
                peso = peso,
                cajas = cajas
            )
        }

        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            repo.create(
                nroSerie = serieGuia,          // revisa tu semántica: serieGuia → nroSerie
                nroGuia = nroGuia,
                observacion = observaciones.ifBlank { null },
                clientId = client.id,
                storeId = store.id,
                userId = currentUserId,
                idReason = reason.id,
                detalles = detallePairs
            )
                .onSuccess { activity: Activity ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        createdActivity = activity
                    )
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Error al crear actividad"
                    )
                }
        }
    }

    private fun setError(msg: String) {
        _state.value = _state.value.copy(isLoading = false, error = msg)
    }
}
