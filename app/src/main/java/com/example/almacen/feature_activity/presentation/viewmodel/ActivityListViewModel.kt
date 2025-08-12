package com.example.almacen.feature_activity.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacen.feature_activity.domain.repository.ActivityRepository
import com.example.almacen.feature_activity.presentation.state.ActivityListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityListViewModel @Inject constructor(
    private val repo: ActivityRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ActivityListState())
    val state: StateFlow<ActivityListState> = _state

    fun loadActivities() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            repo.list()
                .onSuccess { list ->
                    _state.value = _state.value.copy(isLoading = false, activities = list)
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
        }
    }
}
