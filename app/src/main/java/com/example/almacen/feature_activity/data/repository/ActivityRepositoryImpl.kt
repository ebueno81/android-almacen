// com/example/almacen/feature_activity/data/repository/ActivityRepositoryImpl.kt
package com.example.almacen.feature_activity.data.repository

import com.example.almacen.core.session.RememberSession
import com.example.almacen.feature_activity.data.mapper.toDomain
import com.example.almacen.feature_activity.data.remote.api.ActivityApi
import com.example.almacen.feature_activity.data.remote.dto.ActivityDetailRequest
import com.example.almacen.feature_activity.data.remote.dto.ActivityRequest
import com.example.almacen.feature_activity.domain.model.Activity
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

    override suspend fun get(id: Long): Result<Activity> = runCatching {
        api.get(id).toDomain()
    }

    override suspend fun create(
        nroSerie: String,
        nroGuia: String,
        observacion: String?,
        clientId: String,
        storeId: String,
        userId: String,
        idReason: String,
        detalles: List<Pair<Long, ActivityFormDetail>>
    ): Result<Activity> = runCatching {
        // lee el código de usuario desde DataStore (remember)
        val userCode = sanitizeUser10(rememberSession.userCodeFlow.first())
        // (opcional) Log de verificación
        android.util.Log.d("ActivityRepo", "usuarioCreacion='$userCode'")

        val body = ActivityRequest(
            nroSerie = nroSerie,
            nroGuia = nroGuia,
            observacion = observacion,
            idCliente = clientId,
            idAlmacen = storeId,
            idReason = idReason,
            usuarioCreacion = userCode,     // ← se envía el código real
            usuarioModifica = userCode,     // opcional
            detalles = detalles.map { (idArticulo, d) ->
                ActivityDetailRequest(
                    idArticulo = idArticulo, // si backend usa INT
                    nroLote = d.lote,
                    peso = d.peso,
                    cajas = d.cajas
                )
            }
        )
        api.create(body).toDomain()
    }
}
