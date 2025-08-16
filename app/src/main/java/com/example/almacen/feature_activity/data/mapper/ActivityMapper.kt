package com.example.almacen.feature_activity.data.mapper

import com.example.almacen.feature_activity.data.remote.dto.ActivityDto
import com.example.almacen.feature_activity.data.remote.dto.ActivityHeaderDto
import com.example.almacen.feature_activity.domain.model.Activity
import com.example.almacen.feature_activity.domain.model.ActivityDetail
import com.example.almacen.feature_activity.domain.model.ActivityHeader

fun ActivityDto.toDomain() = Activity(
    id = id,
    nroSerie = nroSerie,
    nroGuia = nroGuia,
    observacion = observacion,
    clientId = clientId,
    clientNombre = clientNombre?:"",
    storeId = storeId,
    storeNombre = storeNombre,
    userId = userId?:"",
    userNombre = userNombre?:"",
    idReason = idReason?:"",
    reasonNombre = reasonNombre?:"",
    fechaCreacion = fechaCreacion,
    detalles = detalles.map { d ->
        ActivityDetail(
            id = d.id,
            articuloId = d.idArticulo,
            lote = d.lote,
            peso = d.peso,          // sin ?: 0.0 porque no es null
            cajas = d.cajas,        // sin ?: 0
            nombreArticulo = d.nombreArticulo
        )
    },
    totalPeso = detalles.sumOf { it.peso }   // sin Elvis, coincide con DTO no nulo
)

fun ActivityHeaderDto.toDomain() = ActivityHeader(
    id = id,
    nroSerie = nroSerie,
    nroGuia = nroGuia,
    observacion = observacion,
    clientNombre = clientNombre,
    storeNombre = storeNombre,
    reasonNombre = reasonNombre,
    fechaCreacion = fechaCreacion,
    totalPeso = totalPeso
)
