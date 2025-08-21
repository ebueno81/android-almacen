package com.example.almacen.feature_activity.data.remote.dto

data class ActivityHeaderDto(
    val id: Int,
    val nroSerie: String,
    val nroGuia: String,
    val observacion: String?,
    val clientId: String?,
    val clientNombre: String?,
    val storeId: String?,
    val storeNombre: String?,
    val idReason: String?,
    val reasonNombre: String?,
    val usuarioCreacion: String?,
    val usuarioModifica: String?,
    val fechaCreacion: String,
    val totalPeso: Double,
    val estado: Int
)
