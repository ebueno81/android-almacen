package com.example.almacen.feature_activity.presentation.state

import com.example.almacen.feature_activity.domain.model.Activity

data class NewActivityState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val createdActivity: Activity? = null
)
