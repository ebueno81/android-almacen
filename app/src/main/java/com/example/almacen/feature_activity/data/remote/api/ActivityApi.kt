package com.example.almacen.feature_activity.data.remote.api

import com.example.almacen.feature_activity.data.remote.dto.ActivityDto
import com.example.almacen.feature_activity.data.remote.dto.ActivityRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ActivityApi {

    @GET("api/activity")
    suspend fun list(): List<ActivityDto>

    @GET("api/activity/{id}")
    suspend fun get(@Path("id") id: Long): ActivityDto

    @POST("api/activity")
    suspend fun create(@Body body: ActivityRequest): ActivityDto
}