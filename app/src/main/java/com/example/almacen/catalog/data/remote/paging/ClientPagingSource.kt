package com.example.almacen.catalog.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.almacen.catalog.data.remote.api.CatalogApi
import com.example.almacen.catalog.domain.model.Client

class ClientPagingSource(
    private val api: CatalogApi,
    private val pageSize: Int,
    private val sort: String,
    private val query: String?
) : PagingSource<Int, Client>() {   // <-- ahora Client, no ClientDto

    override fun getRefreshKey(state: PagingState<Int, Client>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Client> = try {
        val page = params.key ?: 0
        val resp = api.getClients(page = page, size = pageSize, sort = sort, q = query)

        val items: List<Client> = resp.content.map { dto ->
            Client(id = dto.id, nombre = dto.nombreCliente)
        }

        val nextKey = if (resp.last == true || items.isEmpty()) null else page + 1
        val prevKey = if (page == 0) null else page - 1

        LoadResult.Page(items, prevKey, nextKey)
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}
