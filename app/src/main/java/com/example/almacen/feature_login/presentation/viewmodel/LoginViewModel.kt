package com.example.almacen.feature_login.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacen.core.datastore.RememberStore
import com.example.almacen.core.datastore.TokenStore
import com.example.almacen.core.security.PasswordVault
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

    var rememberChecked = mutableStateOf(false)
        private set

    fun setRemember(checked: Boolean){
        rememberChecked.value = checked
    }

    fun login(context: Context, user: String, password: String) {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true, error = null)

            val result = loginUseCase(user, password)

            result
                .onSuccess { session ->
                    TokenStore.save(context, session.token, session.username, session.idUser)

                    if (rememberChecked.value){
                        RememberStore.save(context, remember=true, idUser = session.idUser)
                        PasswordVault.save(context, password)
                    }else{
                        RememberStore.save(context, remember=false, idUser = null)
                        PasswordVault.clear(context)
                    }

                    state.value = state.value.copy(
                        isLoading = false,
                        success = true
                    )
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
