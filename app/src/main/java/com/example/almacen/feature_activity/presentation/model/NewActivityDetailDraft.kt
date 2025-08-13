package com.example.almacen.feature_activity.presentation.model

import com.example.almacen.catalog.domain.model.Article

data class NewActivityDetailDraft(
    val articulo: Article? = null,
    val lote: String = "",
    val peso: String = "",
    val cajas: String = ""
)