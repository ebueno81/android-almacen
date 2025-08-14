package com.example.almacen.catalog.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.almacen.catalog.data.remote.api.CatalogApi
import com.example.almacen.catalog.data.remote.paging.ClientPagingSource
import com.example.almacen.catalog.domain.model.Client
import javax.inject.Inject

class SearchClientsUseCase @Inject constructor(
    private val api: CatalogApi
) {
    operator fun invoke(query: String?): Pager<Int, Client> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                ClientPagingSource(
                    api = api,
                    sort = "nombreCliente,ASC",
                    query = query
                )
            }
        )
}
