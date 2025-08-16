package com.example.almacen.feature_activity.domain.repository

import com.example.almacen.feature_activity.domain.model.Activity
import com.example.almacen.feature_activity.domain.model.ActivityDetail
import com.example.almacen.feature_activity.domain.model.ActivityHeader

interface ActivityRepository {

    suspend fun list(): Result<List<Activity>>

    // usa Int si en el backend el ID es int
    suspend fun get(id: Int): Result<Activity>

    // CREA cabecera + detalles iniciales
    suspend fun create(
        nroSerie: String,
        nroGuia: String,
        observacion: String?,
        clientId: String,
        storeId: String,
        idReason: String,
        detalles: List<Pair<Int, ActivityFormDetail>> // idArticulo como Int
    ): Result<Activity>

    // NUEVO: actualiza solo cabecera
    suspend fun updateHeader(
        id: Int,
        nroSerie: String,
        nroGuia: String,
        observacion: String?,
        clientId: String,
        storeId: String,
        idReason: String
    ): Result<Activity>

    // NUEVO: CUD de detalles (upsert)
    suspend fun upsertDetails(
        id: Int,
        toCreate: List<Pair<Int, ActivityFormDetail>>, // idArticulo + datos
        toUpdate: List<Triple<Int, Int, ActivityFormDetail>>, // (idDetalle, idArticulo, data)
        toDelete: List<Int> // ids de detalle a borrar
    ): Result<Activity>

    suspend fun updateDetail(
        detailId: Long,
        articuloId: Long,
        lote: String,
        peso: Double,
        cajas: Int
    ): Result<ActivityDetail>

    suspend fun listHeaders(): Result<List<ActivityHeader>>

}

data class ActivityFormDetail(
    val lote: String,
    val peso: Double,
    val cajas: Int
)
