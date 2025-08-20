// ActivityHeadersPagingSource.kt
package com.example.almacen.feature_activity.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.almacen.feature_activity.domain.model.ActivityHeader
import com.example.almacen.feature_activity.domain.repository.ActivityRepository

class ActivityHeadersPagingSource(
    private val repo: ActivityRepository,
    private val nombreCliente: String?,  // filtro opcional
    private val pageSize: Int            // ej. 20
) : PagingSource<Int, ActivityHeader>() {

    override fun getRefreshKey(state: PagingState<Int, ActivityHeader>): Int? {
        // punto de anclaje para refresco
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ActivityHeader> {
        val pageIndex = params.key ?: 0
        return try {
            // tu repo debe llamar al endpoint: /api/activity/headers?page=pageIndex&size=pageSize&nombreCliente=...
            val result = repo.listHeaders(
                page = pageIndex,
                size = pageSize,
                nombreCliente = nombreCliente
            )
            result.fold(
                onSuccess = { list ->
                    val nextKey = if (list.size < pageSize) null else pageIndex + 1
                    val prevKey = if (pageIndex == 0) null else pageIndex - 1
                    LoadResult.Page(
                        data = list,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                },
                onFailure = { e -> LoadResult.Error(e) }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
