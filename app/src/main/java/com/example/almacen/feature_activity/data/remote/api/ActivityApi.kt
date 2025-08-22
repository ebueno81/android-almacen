package com.example.almacen.feature_activity.data.remote.api

import com.example.almacen.catalog.data.remote.dto.PageResponse
import com.example.almacen.feature_activity.data.remote.dto.ActivityDetailDto
import com.example.almacen.feature_activity.data.remote.dto.ActivityDetailRequest
import com.example.almacen.feature_activity.data.remote.dto.ActivityDto
import com.example.almacen.feature_activity.data.remote.dto.ActivityHeaderDto
import com.example.almacen.feature_activity.data.remote.dto.ActivityRequest
import com.example.almacen.feature_activity.data.remote.dto.ProcessActivityResponseDto
import com.example.almacen.feature_activity.data.remote.dto.UpdateActivityHeaderRequest
import com.example.almacen.feature_activity.data.remote.dto.UpsertActivityDetailsRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @PUT("api/activity/details/{detailId}")
    suspend fun updateDetail(
        @Path("detailId") detailId: Long,
        @Body body: ActivityDetailRequest
    ): ActivityDetailDto

    @GET("api/activity/headers")
    suspend fun getActivitiesHeaders(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("nombreCliente") nombreCliente: String? = null
    ): PageResponse<ActivityHeaderDto>

    @POST("api/activity/{id}/process")
    suspend fun processActivity(
        @Path("id") id: Int,
        @Query("user") user: String? = null
    ): ProcessActivityResponseDto
}