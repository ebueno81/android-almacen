package com.example.almacen.feature_login.data.repository

import com.example.almacen.feature_login.data.model.UserCredentials
import com.example.almacen.feature_login.data.remote.api.AuthApi
import com.example.almacen.feature_login.data.remote.dto.LoginRequestDto
import com.example.almacen.feature_login.domain.model.AuthSession
import com.example.almacen.feature_login.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
): AuthRepository {
    override suspend fun login(credentials: UserCredentials): Result<AuthSession> = try{
        val res = api.login(LoginRequestDto(credentials.username, credentials.password))
        Result.success(AuthSession(token = res.token, username = res.nameUser, idUser = res.idUser))
    }catch (e: Exception){
        Result.failure(e)
    }
}