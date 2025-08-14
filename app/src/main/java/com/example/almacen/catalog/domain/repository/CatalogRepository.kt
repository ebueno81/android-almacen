package com.example.almacen.catalog.domain.repository

import androidx.paging.PagingData
import com.example.almacen.catalog.domain.model.Article
import com.example.almacen.catalog.domain.model.Client
import com.example.almacen.catalog.domain.model.Reason
import com.example.almacen.catalog.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    suspend fun getStores(): List<Store>
    suspend fun getReasons(): List<Reason>
    suspend fun getArticles(): List<Article>

    fun searchClients(
        pageSize: Int = 20,
        sort: String = "nombreCliente,ASC",
        query: String? = null
    ): Flow<PagingData<Client>>
}
