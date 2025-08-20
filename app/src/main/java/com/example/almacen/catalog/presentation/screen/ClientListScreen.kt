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
import com.example.almacen.catalog.domain.model.Client
import com.example.almacen.catalog.presentation.screen.components.ClientItem
import com.example.almacen.catalog.presentation.viewmodel.ClientListViewModel
import com.example.almacen.core.ui.components.AppScaffold

@Composable
fun ClientListScreen(
    onBack: () -> Unit,
    vm: ClientListViewModel = hiltViewModel()
) {
    val lazyItems = vm.pagingFlow.collectAsLazyPagingItems()
    var query by remember { mutableStateOf("") }

    AppScaffold(
        title = "Clientes",
        onBack = onBack
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it; vm.onQueryChange(it) },
                singleLine = true,
                label = { Text("Buscar cliente…") },   // ← sin restricción
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
                                ClientItem(client = c) // ocupa todo el ancho con fondo claro
                            }
                        }

                        // Footer de append (paginación)
                        when (val ap = lazyItems.loadState.append) {
                            is LoadState.Loading -> item {
                                Text("Cargando más…", Modifier.padding(12.dp))
                            }
                            is LoadState.Error -> item {
                                Text("Error al cargar más: ${ap.error.message}")
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}
