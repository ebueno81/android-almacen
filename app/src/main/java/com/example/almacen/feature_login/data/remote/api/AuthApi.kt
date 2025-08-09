package com.example.almacen.feature_login.data.remote.api

import com.example.almacen.feature_login.data.remote.dto.LoginRequestDto
import com.example.almacen.feature_login.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequestDto): LoginResponseDto
}