package com.example.almacen.feature_activity.data.remote.api

import com.example.almacen.feature_activity.data.remote.dto.ActivityDto
import com.example.almacen.feature_activity.data.remote.dto.ActivityRequest
import com.example.almacen.feature_activity.data.remote.dto.UpdateActivityHeaderRequest
import com.example.almacen.feature_activity.data.remote.dto.UpsertActivityDetailsRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ActivityApi {

    @GET("api/activity")
    suspend fun list(): List<ActivityDto>

    @GET("api/activity/{id}")
    suspend fun get(@Path("id") id: Int): ActivityDto

    @POST("api/activity")
    suspend fun create(@Body body: ActivityRequest): ActivityDto

    @PUT("api/activity/{id}")
    suspend fun updateHeader(
        @Path("id") id: Int,
        @Body body: UpdateActivityHeaderRequest
    ): ActivityDto

    // NUEVO: upsert (crear/actualizar/borrar) detalles
    @PUT("api/activity/{id}/details")
    suspend fun upsertDetails(
        @Path("id") id: Int,
        @Body body: UpsertActivityDetailsRequest
    ): ActivityDto
}