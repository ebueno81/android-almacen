package com.example.almacen.feature_activity.presentation.model

import com.example.almacen.catalog.domain.model.Client
import com.example.almacen.catalog.domain.model.Reason
import com.example.almacen.catalog.domain.model.Store

data class NewActivityDraft(
    val client: Client?,
    val store: Store?,
    val reason: Reason?,
    val nroGuia: String,
    val serieGuia: String,
    val observaciones: String,
    val detalles: List<NewActivityDetailDraft>
)

