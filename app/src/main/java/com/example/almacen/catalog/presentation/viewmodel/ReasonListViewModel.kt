package com.example.almacen.catalog.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacen.catalog.domain.model.Reason
import com.example.almacen.catalog.domain.usecase.GetReasonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReasonListViewModel @Inject constructor(
    private val getReasons: GetReasonsUseCase
) : ViewModel() {

    var all by mutableStateOf<List<Reason>>(emptyList()); private set
    var query by mutableStateOf("")

    init {
        viewModelScope.launch {
            try { all = getReasons() } catch (_: Exception) {}
        }
    }

    val filtered: List<Reason>
        get() = if (query.isBlank()) all
        else all.filter { it.nombre.contains(query, ignoreCase = true) }

    fun onQueryChange(q: String) { query = q }
}