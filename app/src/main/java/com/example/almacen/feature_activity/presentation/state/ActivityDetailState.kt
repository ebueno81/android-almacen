package com.example.almacen.feature_activity.presentation.state

import com.example.almacen.feature_activity.domain.model.Activity

data class ActivityDetailState(
    val isLoading: Boolean = false,
    val activity: Activity? = null,
    val error: String? = null
)
