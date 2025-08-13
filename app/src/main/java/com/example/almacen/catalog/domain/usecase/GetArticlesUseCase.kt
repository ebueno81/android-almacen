package com.example.almacen.catalog.domain.usecase

import com.example.almacen.catalog.domain.repository.CatalogRepository
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(private val repo: CatalogRepository) {
    suspend operator fun invoke() = repo.getArticles()
}

