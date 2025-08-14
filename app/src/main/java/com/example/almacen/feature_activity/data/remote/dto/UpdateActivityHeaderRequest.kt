package com.example.almacen.feature_activity.data.remote.dto

data class UpdateActivityHeaderRequest(
    val nroSerie: String,
    val nroGuia: String,
    val observacion: String?,
    val idCliente: String,
    val idAlmacen: String,
    val idReason: String,
    val usuarioModifica: String
)