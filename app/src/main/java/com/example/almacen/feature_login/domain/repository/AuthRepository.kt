package com.example.almacen.feature_login.domain.repository

import com.example.almacen.feature_login.data.model.UserCredentials
import com.example.almacen.feature_login.domain.model.AuthSession

interface AuthRepository {
    suspend fun login(credentials: UserCredentials): Result<AuthSession>
}