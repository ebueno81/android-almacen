package com.example.almacen.feature_activity.domain.usecase

import com.example.almacen.feature_activity.domain.model.Activity
import com.example.almacen.feature_activity.domain.repository.ActivityFormDetail
import com.example.almacen.feature_activity.domain.repository.ActivityRepository
import javax.inject.Inject

class ListActivities @Inject constructor(
    private val repo: ActivityRepository
) {
    suspend operator fun invoke(): Result<List<Activity>> = repo.list()
}

class GetActivity @Inject constructor(
    private val repo: ActivityRepository
) {
    suspend operator fun invoke(id: Int): Result<Activity> = repo.get(id)
}

class CreateActivity @Inject constructor(
    private val repo: ActivityRepository
) {
    suspend operator fun invoke(
        nroSerie: String,
        nroGuia: String,
        observacion: String?,
        clientId: String,
        storeId: String,
        idReason: String,
        detalles: List<Pair<Int, ActivityFormDetail>>
    ): Result<Activity> = repo.create(
        nroSerie = nroSerie,
        nroGuia = nroGuia,
        observacion = observacion,
        clientId = clientId,
        storeId = storeId,
        idReason = idReason,
        detalles = detalles
    )
}

/** Actualiza solo la cabecera de la actividad existente */
class UpdateActivityHeader @Inject constructor(
    private val repo: ActivityRepository
) {
    suspend operator fun invoke(
        id: Int,
        nroSerie: String,
        nroGuia: String,
        observacion: String?,
        clientId: String,
        storeId: String,
        idReason: String
    ): Result<Activity> = repo.updateHeader(
        id = id,
        nroSerie = nroSerie,
        nroGuia = nroGuia,
        observacion = observacion,
        clientId = clientId,
        storeId = storeId,
        idReason = idReason
    )
}

/**
 * Upsert (crear / actualizar / eliminar) detalles de una actividad:
 * - toCreate: pares (idArticulo, datos)
 * - toUpdate: triples (idDetalle, idArticulo, datos)
 * - toDelete: ids de detalle a eliminar
 */
class UpsertActivityDetails @Inject constructor(
    private val repo: ActivityRepository
) {
    suspend operator fun invoke(
        id: Int,
        toCreate: List<Pair<Int, ActivityFormDetail>>,
        toUpdate: List<Triple<Int, Int, ActivityFormDetail>>,
        toDelete: List<Int>
    ): Result<Activity> = repo.upsertDetails(
        id = id,
        toCreate = toCreate,
        toUpdate = toUpdate,
        toDelete = toDelete
    )
}
