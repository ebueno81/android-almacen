package com.example.almacen.feature_activity.data.remote.dto

data class ActivityDetailDto(
    val id: Long,
    val idArticulo: Long,
    val lote: String,
    val peso: Double,           // no nulo
    val cajas: Int,             // no nulo
    val nombreArticulo: String? = null
)