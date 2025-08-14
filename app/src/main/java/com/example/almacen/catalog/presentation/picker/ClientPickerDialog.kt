package com.example.almacen.catalog.presentation.picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.almacen.catalog.domain.model.Client
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientPickerDialog(
    onDismiss: () -> Unit,
    onSelected: (Client) -> Unit,
    pagingFlow: Flow<PagingData<Client>>,
    onQueryChange: (String?) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val canSearch = query.trim().length >= 3

    // Importante: solo notificar al VM cuando hay 3+ letras; si no, null (lista vacía)
    LaunchedEffect(query) {
        onQueryChange(if (canSearch) query.trim() else null)
    }

    val clients = pagingFlow.collectAsLazyPagingItems()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Seleccionar cliente") },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar… (mín. 3 letras)") },
                    singleLine = true,
                    trailingIcon = { Icon(Icons.Filled.Search, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                // Si todavía no se puede buscar, NO mostramos la lista ni el spinner
                if (!canSearch) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Escribe al menos 3 letras para buscar",
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    return@AlertDialog
                }

                // A partir de aquí sí mostramos resultados / estados de carga
                LazyColumn(Modifier.height(420.dp)) {
                    items(clients.itemCount) { idx ->
                        clients[idx]?.let { c ->
                            ListItem(
                                headlineContent = { Text(c.nombre) },
                                supportingContent = { Text("ID: ${c.id}") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelected(c) }
                                    .padding(vertical = 6.dp)
                            )
                            Divider()
                        }
                    }

                    when {
                        clients.loadState.refresh is LoadState.Loading ->
                            item { CenteredProgress() }
                        clients.loadState.append is LoadState.Loading ->
                            item { CenteredProgress() }
                        clients.loadState.refresh is LoadState.Error ->
                            item { ErrorRow("Error cargando") }
                        clients.loadState.append is LoadState.Error ->
                            item { ErrorRow("Error al paginar") }
                        clients.itemCount == 0 && clients.loadState.refresh is LoadState.NotLoading ->
                            item {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) { Text("Sin resultados para “${query.trim()}”") }
                            }
                    }
                }
            }
        }
    )
}

@Composable
private fun CenteredProgress() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
private fun ErrorRow(msg: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) { Text(msg) }
}
