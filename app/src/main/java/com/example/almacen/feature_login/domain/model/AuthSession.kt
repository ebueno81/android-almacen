package com.example.almacen.feature_login.domain.model

data class AuthSession(
    val token: String,
    val username: String,
    val idUser: String
)