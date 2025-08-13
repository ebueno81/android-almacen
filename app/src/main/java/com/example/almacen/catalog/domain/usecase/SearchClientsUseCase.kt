package com.example.almacen.catalog.domain.usecase

import androidx.paging.PagingData
import com.example.almacen.catalog.domain.model.Client
import com.example.almacen.catalog.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchClientsUseCase @Inject constructor(
    private val repo: CatalogRepository
) {
    operator fun invoke(query: String?): Flow<PagingData<Client>> =
        repo.searchClients(query = query) // <- Flow<PagingData<Client>>
}
