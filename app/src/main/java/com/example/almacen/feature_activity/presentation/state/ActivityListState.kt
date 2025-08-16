package com.example.almacen.feature_activity.presentation.state

import com.example.almacen.feature_activity.domain.model.ActivityHeader

data class ActivityListState(
    val isLoading: Boolean = false,
    val headers: List<ActivityHeader> = emptyList(),
    val error: String? = null
)