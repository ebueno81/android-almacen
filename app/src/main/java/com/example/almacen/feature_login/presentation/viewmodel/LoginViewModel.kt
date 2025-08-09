package com.example.almacen.feature_login.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacen.core.datastore.TokenStore
import com.example.almacen.feature_login.domain.usecase.LoginUseCase
import com.example.almacen.feature_login.presentation.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var state = mutableStateOf(LoginUiState())
        private set

    fun login(context: Context, user: String, pass: String) {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true, error = null)

            val result = loginUseCase(user, pass) // kotlin.Result<String>

            result
                .onSuccess { token ->
                    TokenStore.save(context, token)
                    state.value = state.value.copy(isLoading = false, success = true)
                }
                .onFailure { e ->
                    state.value = state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Error"
                    )
                }
        }
    }
}
