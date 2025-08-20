package com.example.almacen.catalog.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.almacen.catalog.domain.model.Article
import com.example.almacen.catalog.domain.usecase.GetArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val getArticles: GetArticlesUseCase
) : ViewModel() {

    var query by mutableStateOf<String?>(null)
        private set

    val pagingFlow: Flow<PagingData<Article>> =
        snapshotFlow { query }
            .map { it?.trim() }
            .debounce(300)
            .flatMapLatest { q ->
                val effective = q ?: ""
                getArticles(effective).flow
            }
            .cachedIn(viewModelScope)

    fun onQueryChange(q: String?) { query = q }
}
