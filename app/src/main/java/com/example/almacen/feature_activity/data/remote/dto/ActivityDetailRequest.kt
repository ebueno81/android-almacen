package com.example.almacen.feature_activity.data.remote.dto

data class ActivityDetailRequest(
    val idArticulo: Int,
    val nroLote: String,
    val peso: Double,
    val cajas: Int
)