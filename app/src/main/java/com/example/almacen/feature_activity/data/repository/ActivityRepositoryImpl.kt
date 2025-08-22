package com.example.almacen.feature_activity.data.repository

import com.example.almacen.core.session.RememberSession
import com.example.almacen.feature_activity.data.mapper.toDomain
import com.example.almacen.feature_activity.data.remote.api.ActivityApi
import com.example.almacen.feature_activity.data.remote.dto.ActivityDetailRequest
import com.example.almacen.feature_activity.data.remote.dto.ActivityRequest
import com.example.almacen.feature_activity.data.remote.dto.UpdateActivityHeaderRequest
import com.example.almacen.feature_activity.data.remote.dto.UpsertActivityDetailsRequest
import com.example.almacen.feature_activity.domain.model.Activity
import com.example.almacen.feature_activity.domain.model.ActivityDetail
import com.example.almacen.feature_activity.domain.model.ActivityHeader
import com.example.almacen.feature_activity.domain.repository.ActivityFormDetail
import com.example.almacen.feature_activity.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val USUARIO_MAX = 10
private fun sanitizeUser10(s: String?): String =
    (s ?: "").replace("\t", " ").trim().uppercase().take(USUARIO_MAX)

class ActivityRepositoryImpl @Inject constructor(
    private val api: ActivityApi,
    private val rememberSession: RememberSession
) : ActivityRepository {

    override suspend fun list(): Result<List<Activity>> = runCatching {
        api.list().map { it.toDomain() }
    }

    override suspend fun get(id: Int): Result<Activity> = runCatching {
        api.get(id).toDomain()
    }

    // ==== CREATE ====
    override suspend fun create(
        nroSerie: String,
        nroGuia: String,
        observacion: String?,
        clientId: String,
        storeId: String,
        idReason: String,
        detalles: List<Pair<Int, ActivityFormDetail>>
    ): Result<Activity> = runCatching {
        val userCode = sanitizeUser10(rememberSession.userCodeFlow.first())
        val body = ActivityRequest(
            nroSerie = nroSerie,
            nroGuia = nroGuia,
            observacion = observacion,
            idCliente = clientId,
            idAlmacen = storeId,
            idReason = idReason,
            usuarioCreacion = userCode,
            usuarioModifica = userCode,
            detalles = detalles.map { (idArticulo, d) ->
                ActivityDetailRequest(
                    idArticulo = idArticulo.toLong(),
                    nroLote = d.lote,
                    peso = d.peso,
                    cajas = d.cajas
                )
            }
        )
        api.create(body).toDomain()
    }

    // ==== UPDATE HEADER ====
    override suspend fun updateHeader(
        id: Int,
        nroSerie: String,
        nroGuia: String,
        observacion: String?,
        clientId: String,
        storeId: String,
        idReason: String
    ): Result<Activity> = runCatching {
        val userCode = sanitizeUser10(rememberSession.userCodeFlow.first())
        val body = UpdateActivityHeaderRequest(
            nroSerie = nroSerie,
            nroGuia = nroGuia,
            observacion = observacion,
            idCliente = clientId,
            idAlmacen = storeId,
            idReason = idReason,
            usuarioModifica = userCode
        )
        api.updateHeader(id, body).toDomain()
    }

    // ==== UPSERT DETAILS ====
    override suspend fun upsertDetails(
        id: Int,
        toCreate: List<Pair<Int, ActivityFormDetail>>,
        toUpdate: List<Triple<Int, Int, ActivityFormDetail>>,
        toDelete: List<Int>
    ): Result<Activity> = runCatching {
        val req = UpsertActivityDetailsRequest(
            toCreate = toCreate.map { (idArticulo, d) ->
                UpsertActivityDetailsRequest.DetailToCreate(
                    idArticulo = idArticulo,
                    nroLote = d.lote,
                    peso = d.peso,
                    cajas = d.cajas
                )
            },
            toUpdate = toUpdate.map { (idDetalle, idArticulo, d) ->
                UpsertActivityDetailsRequest.DetailToUpdate(
                    id = idDetalle,
                    idArticulo = idArticulo,
                    nroLote = d.lote,
                    peso = d.peso,
                    cajas = d.cajas
                )
            },
            toDelete = toDelete
        )
        api.upsertDetails(id, req).toDomain()
    }

    // ==== UPDATE DETAIL ====
    override suspend fun updateDetail(
        detailId: Long,
        articuloId: Long,
        lote: String,
        peso: Double,
        cajas: Int
    ): Result<ActivityDetail> = runCatching {
        val body = ActivityDetailRequest(
            idArticulo = articuloId,
            nroLote = lote,
            peso = peso,
            cajas = cajas
        )
        // Llamada a la API y mapeo con ActivityDetailMapper
        api.updateDetail(detailId, body).toDomain()
    }

    override suspend fun listHeaders(
        page: Int,
        size: Int,
        nombreCliente: String?
    ): Result<List<ActivityHeader>> = runCatching {
        val resp = api.getActivitiesHeaders(
            page = page,
            size = size,
            nombreCliente = nombreCliente // 👈 mapea al nombre real del backend
        )
        resp.content.map { it.toDomain() } // convierte tu DTO a modelo domain
    }

    override suspend fun processActivity(id: Int, user: String): Result<Int> = runCatching {
        val resp = api.processActivity(id, user)
        resp.idIngresoCreado
    }

    // ====== (Opcional) Backward compatibility ======
    // Si en tu ViewModel todavía llamas a get(Long) o create con userId, déjalos como “puentes”:
    @Deprecated("Usar get(Int)")
    suspend fun getLegacy(id: Long): Result<Activity> = get(id.toInt())

    @Deprecated("userId se toma del RememberSession automáticamente")
    suspend fun createLegacy(
        nroSerie: String,
        nroGuia: String,
        observacion: String?,
        clientId: String,
        storeId: String,
        userId: String, // ignorado
        idReason: String,
        detalles: List<Pair<Long, ActivityFormDetail>>
    ): Result<Activity> =
        create(
            nroSerie, nroGuia, observacion, clientId, storeId, idReason,
            detalles = detalles.map { (idArtLong, d) -> idArtLong.toInt() to d }
        )
}
