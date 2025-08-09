package com.example.almacen.feature_login.domain.repository

import com.example.almacen.feature_login.data.model.UserCredentials
import com.example.almacen.feature_login.data.remote.api.AuthApi
import com.example.almacen.feature_login.data.remote.dto.LoginRequestDto
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
): AuthRepository {
    override suspend fun login(credentials: UserCredentials): Result<String> = try{
        val res = api.login(LoginRequestDto(credentials.username, credentials.password))
        Result.success(res.token)
    }catch (e: Exception){
        Result.failure(e)
    }
}