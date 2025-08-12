package com.example.almacen.feature_activity.presentation.state

import com.example.almacen.feature_activity.domain.model.Activity

data class ActivityListState(
    val isLoading: Boolean = false,
    val activities: List<Activity> = emptyList(),
    val error: String? = null
)