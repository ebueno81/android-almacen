package com.example.almacen.catalog.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.almacen.catalog.data.remote.api.CatalogApi
import com.example.almacen.catalog.domain.model.Client

class ClientPagingSource(
    private val api: CatalogApi,
    private val sort: String,
    private val query: String?
) : PagingSource<Int, Client>() {

    override fun getRefreshKey(state: PagingState<Int, Client>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Client> = try {
        val page = params.key ?: 0
        val size = params.loadSize.coerceAtMost(50) // o tu pageSize si quieres fijo

        val resp = api.getClients(
            page = page,
            size = size,
            q = query?.takeIf { it.isNotBlank() },
            sort = sort
        )

        val items: List<Client> = resp.content.orEmpty().map { dto ->
            Client(
                id = dto.id,                       // asegúrate que el DTO tenga estos campos
                nombre = dto.nombreCliente ?: ""   // null-safe
            )
        }

        val isLast = resp.last == true || items.isEmpty()
        val nextKey = if (isLast) null else page + 1
        val prevKey = if (page == 0) null else page - 1

        LoadResult.Page(
            data = items,
            prevKey = prevKey,
            nextKey = nextKey
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}
