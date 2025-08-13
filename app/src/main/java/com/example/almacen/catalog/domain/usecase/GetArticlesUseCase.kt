package com.example.almacen.catalog.domain.usecase

import com.example.almacen.catalog.domain.repository.CatalogRepository

class GetArticlesUseCase(private val repo: CatalogRepository) {
    suspend operator fun invoke() = repo.getArticles()
}

