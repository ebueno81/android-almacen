package com.example.almacen.feature_activity.data.remote.dto

data class ProcessActivityResponseDto(
    val activityId: Int,
    val idIngresoCreado: Int,
    val status: String
)