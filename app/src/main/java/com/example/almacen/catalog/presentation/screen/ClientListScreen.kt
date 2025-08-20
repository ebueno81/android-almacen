package com.example.almacen.catalog.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.almacen.catalog.domain.model.Client
import com.example.almacen.catalog.presentation.screen.components.ClientItem
import com.example.almacen.catalog.presentation.viewmodel.ClientListViewModel

@Composable
fun ClientListScreen(
    modifier: Modifier = Modifier,                // <- AGREGA modifier
    vm: ClientListViewModel = hiltViewModel()
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
            label = { Text("Buscar cliente…") },
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
                    items(count = lazyItems.itemCount) { index ->
                        val c: Client? = lazyItems[index]
                        if (c != null) {
                            ClientItem(client = c)
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
