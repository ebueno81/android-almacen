package com.example.almacen.catalog.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.almacen.catalog.domain.model.Store
import com.example.almacen.catalog.domain.usecase.GetStoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreListViewModel @Inject constructor(
    private val getStores: GetStoresUseCase
) : ViewModel() {

    var all by mutableStateOf<List<Store>>(emptyList()); private set
    var query by mutableStateOf("")

    init {
        viewModelScope.launch {
            try { all = getStores() } catch (_: Exception) {}
        }
    }

    val filtered: List<Store>
        get() = if (query.isBlank()) all
        else all.filter { it.nombre.contains(query, ignoreCase = true) }

    fun onQueryChange(q: String) { query = q }
}
