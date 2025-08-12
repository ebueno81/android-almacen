package com.example.almacen.feature_activity.data.remote.dto

data class ActivityDetailRequest(
    val idArticulo: Long,
    val lote: String,
    val peso: Double,
    val cajas: Int,
    val nombreArticulo: String
)