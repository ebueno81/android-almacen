package com.example.almacen.feature_activity.domain.usecase

import com.example.almacen.feature_activity.domain.repository.ActivityFormDetail
import com.example.almacen.feature_activity.domain.repository.ActivityRepository
import javax.inject.Inject

class ListActivities @Inject constructor(private val repo: ActivityRepository) {
    suspend operator fun invoke() = repo.list()
}
class GetActivity @Inject constructor(private val repo: ActivityRepository) {
    suspend operator fun invoke(id: Long) = repo.get(id)
}
class CreateActivity @Inject constructor(private val repo: ActivityRepository) {
    suspend operator fun invoke(
        nroSerie: String,
        nroGuia: String,
        observacion: String?,
        clientId: String,
        storeId: String,
        userId: String,
        idReason: String,
        detalles: List<Pair<Long, ActivityFormDetail>>
    ) = repo.create(nroSerie, nroGuia, observacion, clientId, storeId, userId, idReason, detalles)
}
