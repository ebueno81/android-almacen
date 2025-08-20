package com.example.almacen.catalog.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.almacen.catalog.domain.model.Article
import com.example.almacen.catalog.presentation.screen.components.ArticleItem
import com.example.almacen.catalog.presentation.viewmodel.ArticleListViewModel

@Composable
fun ArticleListScreen(
    modifier: Modifier = Modifier,
    vm: ArticleListViewModel = hiltViewModel()
) {
    val lazyItems = vm.pagingFlow.collectAsLazyPagingItems()
    var query by remember { mutableStateOf("") }

    Column(
        modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it; vm.onQueryChange(it) },
            singleLine = true,
            label = { Text("Buscar artículo (mín. 3 letras)") },
            trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        when (val st = lazyItems.loadState.refresh) {
            is LoadState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
            is LoadState.Error   -> Text("Error: ${st.error.message}")
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(lazyItems.itemCount) { index ->
                        val art: Article? = lazyItems[index]
                        if (art != null) {
                            ArticleItem(article = art)
                        }
                    }
                    when (val ap = lazyItems.loadState.append) {
                        is LoadState.Loading -> item { Text("Cargando más…", Modifier.padding(12.dp)) }
                        is LoadState.Error   -> item { Text("Error al cargar más: ${ap.error.message}") }
                        else -> Unit
                    }
                }
            }
        }
    }
}
