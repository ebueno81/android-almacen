package com.example.almacen.feature_activity.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacen.feature_activity.domain.repository.ActivityRepository
import com.example.almacen.feature_activity.presentation.state.ActivityDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityDetailViewModel @Inject constructor(
    private val repo: ActivityRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ActivityDetailState())
    val state: StateFlow<ActivityDetailState> = _state

    fun loadActivity(id: Long) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            repo.get(id)
                .onSuccess { activity ->
                    _state.value = _state.value.copy(isLoading = false, activity = activity)
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(isLoading = false, error = e.message)
                }
        }
    }
}
