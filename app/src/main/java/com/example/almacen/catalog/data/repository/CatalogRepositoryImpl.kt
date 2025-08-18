package com.example.almacen.catalog.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.almacen.catalog.data.remote.api.CatalogApi
import com.example.almacen.catalog.data.remote.paging.ArticlePagingSource
import com.example.almacen.catalog.data.remote.paging.ClientPagingSource
import com.example.almacen.catalog.domain.model.Article
import com.example.almacen.catalog.domain.model.Client
import com.example.almacen.catalog.domain.model.Reason
import com.example.almacen.catalog.domain.model.Store
import com.example.almacen.catalog.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow

class CatalogRepositoryImpl(
    private val api: CatalogApi
) : CatalogRepository {

    override suspend fun getStores(): List<Store> =
        api.getStores().map { Store(it.id, it.nombreAlmacen) }

    override suspend fun getReasons(): List<Reason> =
        api.getReasons().map { Reason(it.idReason, it.nameReason, it.typeReason) }

    override fun getArticles(
        pageSize: Int,
        sort: String,
        query: String?
    ): Flow<PagingData<Article>> =
        Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            pagingSourceFactory = {
                ArticlePagingSource(
                    api = api,
                    sort = sort,
                    query = query
                )
            }
        ).flow

    override fun searchClients(
        pageSize: Int,
        sort: String,
        query: String?
    ): Flow<PagingData<Client>> =
        Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            pagingSourceFactory = {
                ClientPagingSource(
                    api = api,
                    sort = sort,
                    query = query
                )
            }
        ).flow
}
