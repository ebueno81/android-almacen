package com.example.almacen.feature_activity.domain.model

data class ActivityDetail(
    val id: Long?,
    val articuloId: Long,
    val lote: String,
    val peso: Double,
    val cajas: Int,
    val nombreArticulo: String? = null
)