package com.example.almacen.feature_activity.domain.model

data class Activity(
    val id: Long,
    val nroSerie: String,
    val nroGuia: String,
    val observacion: String?,
    val clientId: String,
    val clientNombre: String,
    val storeId: String,
    val storeNombre: String,
    val userId: String,
    val userNombre: String,
    val idReason: String,
    val reasonNombre: String,
    val fechaCreacion: String?,
    val detalles: List<ActivityDetail>,
    val totalPeso: Double
)