package com.example.almacen.feature_activity.domain.model

data class ActivityHeader(
    val id: Int,
    val nroSerie: String,
    val nroGuia: String,
    val observacion: String?,
    val clientNombre: String?,
    val storeNombre: String?,
    val reasonNombre: String?,
    val fechaCreacion: String,
    val totalPeso: Double,
    val estado: Int
)
