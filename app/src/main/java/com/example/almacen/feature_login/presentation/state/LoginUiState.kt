package com.example.almacen.feature_login.presentation.state

data class LoginUiState(
    val isLoading: Boolean=false,
    val error: String?=null,
    val success: Boolean = false
)