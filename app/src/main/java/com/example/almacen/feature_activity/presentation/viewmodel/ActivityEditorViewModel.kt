package com.example.almacen.feature_activity.presentation.viewmodel

import android.util.Log
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
import com.example.almacen.feature_activity.domain.repository.ActivityFormDetail
import com.example.almacen.feature_activity.domain.repository.ActivityRepository
import com.example.almacen.feature_activity.presentation.model.NewActivityDetailDraft
import com.example.almacen.feature_activity.presentation.state.NewActivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityEditorViewModel @Inject constructor(
    private val repo: ActivityRepository,
    private val getStores: GetStoresUseCase,
    private val getReasons: GetReasonsUseCase,
    private val getArticles: GetArticlesUseCase,
    private val searchClients: SearchClientsUseCase
) : ViewModel() {

    var showEditFab by mutableStateOf(true)
        private set
    // -------- UI STATE (catálogos) --------
    var stores by mutableStateOf<List<Store>>(emptyList()); private set
    var reasons by mutableStateOf<List<Reason>>(emptyList()); private set
    //var articles by mutableStateOf<List<Article>>(emptyList()); private set

    // -------- Cabecera seleccionada --------
    var selectedArticle by mutableStateOf<Article?>(null); private set
    var selectedClient by mutableStateOf<Client?>(null); private set
    var selectedStore by mutableStateOf<Store?>(null); private set
    var selectedReason by mutableStateOf<Reason?>(null); private set

    var nroGuia by mutableStateOf(""); private set
    var nroSerie by mutableStateOf(""); private set
    var observaciones by mutableStateOf(""); private set

    // -------- Detalles (UI) --------
    var detalles by mutableStateOf<List<NewActivityDetailDraft>>(emptyList()); private set

    // Metadatos internos (alineados por índice con 'detalles')
    private val detailIds = mutableListOf<Int?>()
    private val dirtyIdx = mutableSetOf<Int>()
    private val deletedIdx = mutableSetOf<Int>()

    // -------- Estado general --------
    private val _state = MutableStateFlow(NewActivityState())
    val state: StateFlow<NewActivityState> = _state

    /** id de actividad cuando ya existe (modo edición). Si es null => modo creación */
    var activityId: Int? by mutableStateOf(null); private set

    private var initialized = false
    var readOnly by mutableStateOf(true); private set

    // --- Originales (para fallback al guardar) ---
    private var originalReasonId: String? = null
    private var originalReasonName: String? = null

    // -------- ARTICULOS (Paginh) ------ //
 //   var articles by mutableStateOf<List<Article>>(emptyList()); private set

    var articleQuery by mutableStateOf<String?>(null)
    val articlePagingFlow: Flow<PagingData<Article>> =
        snapshotFlow { articleQuery }
            .map { it?.trim() }
            .debounce(300) // puedes bajar de 3000ms a 300ms para que se sienta más rápido
            .flatMapLatest { q ->
                if (q.isNullOrBlank() || q.length < 3)
                    flowOf(PagingData.empty())
                else
                    getArticles(q).flow  // ✅ igual que searchClients
            }
            .cachedIn(viewModelScope)

    fun onArticleQueryChange(q: String?) { articleQuery = q }
   // fun selectArticle(a: Article) { selectedArticle = a }

    // -------- CLIENTES (Paging) --------
    private var clientQuery by mutableStateOf<String?>(null)
    val clientPagingFlow: Flow<PagingData<Client>> =
        snapshotFlow { clientQuery }
            .map { it?.trim() }          // normaliza espacios
            .debounce(3000)              // 3 segundos de pausa
            .flatMapLatest { q ->
                if (q.isNullOrBlank() || q.length < 3)
                    flowOf(PagingData.empty())
                else
                    searchClients(q).flow
            }
            .cachedIn(viewModelScope)

    fun onClientQueryChange(q: String?) { clientQuery = q }
    fun selectClient(c: Client) { selectedClient = c }

    fun selectStore(s: Store) { selectedStore = s }
    fun selectReason(r: Reason) { selectedReason = r }
    fun onNroGuiaChange(v: String) { nroGuia = v }
    fun onNroSerieChange(v: String) { nroSerie = v }
    fun onObservacionesChange(v: String) { observaciones = v }

    fun enterReadOnly() { readOnly = true }

    fun enterEdit() {
        readOnly = false
        showEditFab = false}

    fun enterView(showEdit: Boolean = true) {
        readOnly = true
        showEditFab = showEdit
    }

    fun initIfNeeded(activityIdFromIntent: Int?) {
        if (initialized) return
        initialized = true

        loadStaticLists()

        if (activityIdFromIntent != null && activityIdFromIntent > 0) {
            openFromList(activityIdFromIntent) // carga y modo lectura
        } else {
            startNew() // modo creación
        }
    }

    fun loadStaticLists() {
        viewModelScope.launch {
            try {
                stores = getStores()
                reasons = getReasons()

                // Reconciliar motivo si estamos en edición y el seleccionado está vacío
                if (activityId != null) {
                    val needsReason =
                        (selectedReason == null) || (selectedReason?.id.isNullOrBlank())
                    if (needsReason) {
                        val resolved = reasons.firstOrNull { it.id == originalReasonId }
                            ?: reasons.firstOrNull { it.nombre.equals(originalReasonName ?: "", true) }
                        if (resolved != null) {
                            selectedReason = resolved
                        }
                    }
                }else
                    applyDefaultsForNewIfNeeded()
            } catch (_: Exception) { }
        }
    }

    fun openFromList(id: Int) {
        enterReadOnly()
        loadForEdit(id)
    }

    fun startNew() {
        activityId = null
        enterEdit()
        selectedClient = null
        selectedStore = null
        selectedReason = null
        nroGuia = ""; nroSerie = ""; observaciones = ""
        detalles = emptyList()
        detailIds.clear(); dirtyIdx.clear(); deletedIdx.clear()
        originalReasonId = null; originalReasonName = null
    }

    /** Cargar actividad existente para editar */
    fun loadForEdit(id: Int) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            repo.get(id)
                .onSuccess { a ->
                    activityId = a.id.toInt()
                    nroSerie = a.nroSerie
                    nroGuia = a.nroGuia
                    observaciones = a.observacion ?: ""

                    selectedClient = Client(id = a.clientId, nombre = a.clientNombre ?: "")
                    selectedStore = stores.firstOrNull { it.id == a.storeId }
                        ?.let { it.copy(nombre = a.storeNombre) }
                        ?: Store(id = a.storeId, nombre = a.storeNombre)

                    // Guardar originales (para fallback)
                    originalReasonId = a.idReason
                    originalReasonName = a.reasonNombre

                    // Intentar resolver contra catálogo si ya está cargado
                    selectedReason =
                        reasons.firstOrNull { it.id == a.idReason }
                            ?: reasons.firstOrNull { it.nombre.equals(a.reasonNombre ?: "", true) }
                                    ?: Reason(id = (a.idReason ?: ""), nombre = a.reasonNombre, tipo = "")

                    detalles = a.detalles.map { srv ->
                        val art = Article(
                            id = srv.articuloId.toInt(),
                            nombre = srv.nombreArticulo ?: "")
                        NewActivityDetailDraft(
                            articulo = art,
                            lote = srv.lote,
                            peso = srv.peso.toString(),
                            cajas = srv.cajas.toString()
                        )
                    }
                    detailIds.clear()
                    detailIds.addAll(a.detalles.map { it.id?.toInt() })
                    dirtyIdx.clear()
                    deletedIdx.clear()

                    _state.value = _state.value.copy(isLoading = false)
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun addDetailRow(prefillFromLast: Boolean = true) {
        val newRow = if (prefillFromLast) {
            // Si hay al menos un detalle, copia el último; si no, uno vacío
            detalles.lastOrNull()?.copy() ?: NewActivityDetailDraft()
        } else {
            NewActivityDetailDraft()
        }

        // Agrega el nuevo detalle
        detalles = detalles + newRow

        // Alinea metadatos con la nueva fila
        detailIds += null           // null => será "create" al guardar
        dirtyIdx += detalles.lastIndex  // opcional; no afecta el flujo de "create"
    }

    fun updateDetailRow(index: Int, newValue: NewActivityDetailDraft) {
        if (index !in detalles.indices) return
        val copy = detalles.toMutableList()
        copy[index] = newValue
        detalles = copy
        if (index !in deletedIdx) dirtyIdx += index
    }

    fun removeDetailRow(index: Int) {
        if (index !in detalles.indices) return
        val hasServerId = detailIds.getOrNull(index) != null
        if (hasServerId) {
            deletedIdx += index
            dirtyIdx -= index
        } else {
            val copy = detalles.toMutableList().also { it.removeAt(index) }
            detalles = copy
            detailIds.removeAt(index)
            dirtyIdx.remove(index)
            deletedIdx.remove(index)
            dirtyIdx.clear(); deletedIdx.clear()
        }
    }

    private fun setError(msg: String) {
        _state.value = _state.value.copy(
            isLoading = false,
            error = msg
        )
        saving = false
        Log.e("ActivityVM", "❌ $msg")
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    private var saving = false
    fun save() {
        Log.d("ActivityVM", "save() vm=${this.hashCode()} activityId=$activityId readOnly=$readOnly")
        if (saving) return

        // 🔹 Validación previa
        validateBeforeSave()?.let { errorMsg -> return setError(errorMsg) }

        _state.value = _state.value.copy(isLoading = true, error = null)
        saving = true

        viewModelScope.launch {
            try {
                val id = activityId
                if (id == null) {
                    // --- CREATE ---
                    val detallePairs = detalles.mapIndexedNotNull { _, d ->
                        val artId = d.articulo?.id ?: return@mapIndexedNotNull null
                        val peso = d.peso.toDoubleOrNull() ?: 0.0
                        val cajas = d.cajas.toIntOrNull() ?: 0
                        artId to ActivityFormDetail(lote = d.lote, peso = peso, cajas = cajas)
                    }

                    val created = repo.create(
                        nroSerie = nroSerie,
                        nroGuia = nroGuia,
                        observacion = observaciones.ifBlank { null },
                        clientId = selectedClient!!.id,
                        storeId = selectedStore!!.id,
                        idReason = selectedReason!!.id, // ya validado
                        detalles = detallePairs
                    ).getOrThrow()

                    activityId = created.id.toInt()
                    readOnly = true
                    _state.value = _state.value.copy(isLoading = false, savedId = created.id)
                } else {
                    // --- UPDATE HEADER ---
                    repo.updateHeader(
                        id = id,
                        nroSerie = nroSerie,
                        nroGuia = nroGuia,
                        observacion = observaciones.ifBlank { null },
                        clientId = selectedClient!!.id,
                        storeId = selectedStore!!.id,
                        idReason = selectedReason!!.id
                    ).getOrThrow()

                    // --- CUD detalles ---
                    val toCreate = buildList {
                        detalles.forEachIndexed { idx, d ->
                            if (detailIds.getOrNull(idx) == null && idx !in deletedIdx) {
                                d.articulo?.id?.let { artId ->
                                    add(artId to ActivityFormDetail(
                                        lote = d.lote,
                                        peso = d.peso.toDoubleOrNull() ?: 0.0,
                                        cajas = d.cajas.toIntOrNull() ?: 0
                                    ))
                                }
                            }
                        }
                    }
                    val toUpdate = buildList {
                        detalles.forEachIndexed { idx, d ->
                            val detId = detailIds.getOrNull(idx)
                            if (detId != null && idx in dirtyIdx && idx !in deletedIdx) {
                                add(Triple(
                                    detId,
                                    d.articulo?.id ?: 0,
                                    ActivityFormDetail(
                                        lote = d.lote,
                                        peso = d.peso.toDoubleOrNull() ?: 0.0,
                                        cajas = d.cajas.toIntOrNull() ?: 0
                                    )
                                ))
                            }
                        }
                    }
                    val toDelete = buildList {
                        deletedIdx.forEach { idx ->
                            detailIds.getOrNull(idx)?.let { add(it) }
                        }
                    }

                    if (toCreate.isNotEmpty() || toUpdate.isNotEmpty() || toDelete.isNotEmpty()) {
                        repo.upsertDetails(id, toCreate, toUpdate, toDelete).getOrThrow()
                    }

                    _state.value = _state.value.copy(isLoading = false, savedId = id.toLong())
                }
            } catch (e: Throwable) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al guardar"
                )
            } finally {
                saving = false
            }
        }
    }

    private fun validateBeforeSave(): String? {
        // Validar cliente
        if (selectedClient == null) return "Selecciona cliente"

        // Validar almacén
        if (selectedStore == null) return "Selecciona almacén"

        // Validar motivo
        val selectedReasonId = selectedReason?.id?.takeIf { it.isNotBlank() }
        val reasonIdForUpdate = selectedReasonId
            ?: originalReasonId?.takeIf { it.isNotBlank() }
            ?: reasons.firstOrNull { it.nombre.equals(originalReasonName ?: "", true) }?.id

        if (reasonIdForUpdate.isNullOrBlank()) return "Selecciona motivo"

        // Validar nroSerie y nroGuia
        if (nroSerie.isBlank()) return "Ingresa serie de guía"
        if (nroGuia.isBlank()) return "Ingresa número de guía"

        // Validar detalles incompletos
        if (detalles.any {
                it.articulo?.id == null &&
                        (it.peso.isNotBlank() || it.cajas.isNotBlank() || it.lote.isNotBlank())
            }
        ) return "Hay detalles sin artículo asignado"

        // Validar al menos un detalle válido
        val detallePairs = detalles.mapNotNull { d ->
            val artId = d.articulo?.id ?: return@mapNotNull null
            artId to d
        }
        if (detallePairs.isEmpty()) return "Agrega al menos un detalle válido"

        return null // ✅ si todo OK
    }

    fun consumeSavedEvent() {
        _state.value = _state.value.copy(savedId = null)
    }

    private fun applyDefaultsForNewIfNeeded() {
        if (activityId != null) return

        if (selectedStore == null)
            selectedStore = stores.firstOrNull { it.id == "01" } ?: stores.firstOrNull()

        if (selectedReason == null)
            selectedReason = reasons.firstOrNull { it.id == "18" } ?: reasons.firstOrNull()
    }

}
