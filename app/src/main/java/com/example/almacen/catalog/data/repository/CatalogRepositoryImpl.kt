package com.example.almacen.catalog.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.almacen.catalog.data.remote.api.CatalogApi
import com.example.almacen.catalog.data.remote.paging.ClientPagingSource
import com.example.almacen.catalog.domain.model.Article
import com.example.almacen.catalog.domain.model.Client
import com.example.almacen.catalog.domain.model.Reason
import com.example.almacen.catalog.domain.model.Store
import com.example.almacen.catalog.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CatalogRepositoryImpl(
    private val api: CatalogApi
) : CatalogRepository {

    override suspend fun getStores(): List<Store> =
        api.getStores().map { Store(it.id, it.nombreAlmacen) }

    override suspend fun getReasons(): List<Reason> =
        api.getReasons().map { Reason(it.idReason, it.nameReason, it.typeReason) }

    override suspend fun getArticles(): List<Article> =
        api.getArticles().map { Article(it.id, it.nombreArticulo) }

    override fun searchClients(
        pageSize: Int,
        sort: String,
        query: String?
    ): Flow<PagingData<Client>> =
        Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            pagingSourceFactory = { ClientPagingSource(api, pageSize, sort, query) }
        )
            .flow
            .map { pagingData ->                      // kotlinx.coroutines.flow.map
                pagingData.map { dto ->               // androidx.paging.map
                    Client(dto.id, dto.nombre)
                }
            }
}
