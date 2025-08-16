package com.example.almacen.feature_activity.data.mapper

import com.example.almacen.feature_activity.data.remote.dto.ActivityDetailDto
import com.example.almacen.feature_activity.domain.model.ActivityDetail

fun ActivityDetailDto.toDomain() = ActivityDetail(
    id = id,
    articuloId = idArticulo,
    lote = lote,
    peso = peso,
    cajas = cajas,
    nombreArticulo = nombreArticulo
)
