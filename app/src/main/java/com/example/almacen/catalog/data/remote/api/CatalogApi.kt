package com.example.almacen.catalog.data.remote.api

import com.example.almacen.catalog.data.remote.dto.ArticleDto
import com.example.almacen.catalog.data.remote.dto.ClientDto
import com.example.almacen.catalog.data.remote.dto.PageResponse
import com.example.almacen.catalog.data.remote.dto.ReasonDto
import com.example.almacen.catalog.data.remote.dto.StoreDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CatalogApi {

    // Ajusta base URL en tu módulo DI
    @GET("/api/store")
    suspend fun getStores(): List<StoreDto>

    @GET("/api/reason")
    suspend fun getReasons(): List<ReasonDto>

    @GET("/api/article")
    suspend fun getArticles(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("q") q: String? = null,
        @Query("sort") sort: String = "nombreArticulo,ASC"
    ): PageResponse<ArticleDto>

    // Clientes paginados: ej. /api/client?page=0&size=20&sort=nombreCliente,ASC
    @GET("/api/client")
    suspend fun getClients(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("q") q: String? = null,
        @Query("sort") sort: String = "nombreCliente,ASC"
    ): PageResponse<ClientDto>
}