package com.example.almacen.feature_activity.data.remote.dto

data class ActivityRequest(
    val nroSerie: String,
    val nroGuia: String,
    val observacion: String?,
    val idCliente: String,
    val idAlmacen: String,
    val idReason: String,
    val usuarioCreacion: String,
    val usuarioModifica: String? = null,
    val fechaCreacion: String? = null,

    val detalles: List<ActivityDetailRequest>
)