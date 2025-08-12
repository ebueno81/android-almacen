package com.example.almacen.feature_activity.data.remote.dto

data class CreateActivityRequest(
    val nroSerie: String,
    val nroGuia: String,
    val observacion: String?,
    val clientId: String,
    val storeId: String,
    val userId: String,
    val detalles: List<ActivityDetailRequest>
)