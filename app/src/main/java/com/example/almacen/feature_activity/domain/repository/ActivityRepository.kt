package com.example.almacen.feature_activity.domain.repository

import com.example.almacen.feature_activity.domain.model.Activity

interface ActivityRepository {

    suspend fun list(): Result<List<Activity>>

    suspend fun get(id: Long): Result<Activity>

    suspend fun create(
        nroSerie: String,
        nroGuia: String,
        observacion: String?,
        clientId: String,
        storeId: String,
        userId: String,
        idReason: String,
        detalles: List<Pair<Long, ActivityFormDetail>>
    ): Result<Activity>
}

data class ActivityFormDetail(
    val lote: String,
    val peso: Double,
    val cajas: Int
)