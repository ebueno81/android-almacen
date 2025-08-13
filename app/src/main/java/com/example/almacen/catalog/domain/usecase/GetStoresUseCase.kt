package com.example.almacen.catalog.domain.usecase

import com.example.almacen.catalog.domain.repository.CatalogRepository
import javax.inject.Inject

class GetStoresUseCase @Inject constructor(private val repo: CatalogRepository) {
    suspend operator fun invoke() = repo.getStores()
}

