package com.example.almacen.catalog.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.almacen.catalog.domain.model.Client
import com.example.almacen.catalog.domain.usecase.SearchClientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ClientListViewModel @Inject constructor(
    private val searchClients: SearchClientsUseCase
) : ViewModel() {

    var query by mutableStateOf<String?>(null)
        private set

    val pagingFlow: Flow<PagingData<Client>> =
        snapshotFlow { query }
            .map { it?.trim() }
            .debounce(300)
            .flatMapLatest { q ->
                if (q.isNullOrBlank())
                    searchClients("").flow
                 else
                    searchClients(q).flow
            }
            .cachedIn(viewModelScope)

    fun onQueryChange(q: String?) { query = q }
}

