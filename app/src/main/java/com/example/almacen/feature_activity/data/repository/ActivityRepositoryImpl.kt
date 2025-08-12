package com.example.almacen.feature_activity.data.repository

import com.example.almacen.feature_activity.data.mapper.toDomain
import com.example.almacen.feature_activity.data.remote.api.ActivityApi
import com.example.almacen.feature_activity.data.remote.dto.ActivityDetailRequest
import com.example.almacen.feature_activity.data.remote.dto.CreateActivityRequest
import com.example.almacen.feature_activity.domain.model.Activity
import com.example.almacen.feature_activity.domain.repository.ActivityFormDetail
import com.example.almacen.feature_activity.domain.repository.ActivityRepository
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val api: ActivityApi
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
        val body = CreateActivityRequest(
            nroSerie = nroSerie,
            nroGuia = nroGuia,
            observacion = observacion,
            clientId = clientId,
            storeId = storeId,
            userId = userId,
            detalles = detalles.map { (idArticulo, d) ->
                ActivityDetailRequest(
                    idArticulo = idArticulo,
                    lote = d.lote,
                    peso = d.peso,
                    cajas = d.cajas,
                    nombreArticulo = TODO()
                )
            }
        )
        api.create(body).toDomain()
    }
}