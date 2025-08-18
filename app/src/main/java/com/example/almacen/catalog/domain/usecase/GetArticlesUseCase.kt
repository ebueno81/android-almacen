package com.example.almacen.catalog.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.almacen.catalog.data.remote.api.CatalogApi
import com.example.almacen.catalog.data.remote.paging.ArticlePagingSource
import com.example.almacen.catalog.domain.model.Article
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
    private val api: CatalogApi
) {
    operator fun invoke(query: String?): Pager<Int, Article> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                ArticlePagingSource(
                    api = api,
                    sort = "nombreArticulo,ASC",
                    query = query
                )
            }
        )
}

