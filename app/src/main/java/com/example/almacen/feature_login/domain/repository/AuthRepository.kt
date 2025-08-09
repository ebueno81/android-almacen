package com.example.almacen.feature_login.domain.repository

import com.example.almacen.feature_login.data.model.UserCredentials

interface AuthRepository {
    suspend fun login(credentials: UserCredentials): Result<String>
}