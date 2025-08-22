package com.example.almacen.feature_activity.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.almacen.core.ui.components.AppCard
import com.example.almacen.feature_activity.domain.model.ActivityHeader
import com.example.almacen.feature_activity.presentation.ui.components.ConfirmDialog
import com.example.almacen.feature_activity.presentation.viewmodel.ActivityListViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ActivityListScreen(
    modifier: Modifier = Modifier,
    onOpenEditor: (Int, Boolean) -> Unit,
    onCreateNew: () -> Unit,
    onAlmacenConfirm: (Int) -> Unit,
    vm: ActivityListViewModel
) {
    val lazyItems = vm.pagingFlow.collectAsLazyPagingItems()

    // 1) Carga inicial
    LaunchedEffect(Unit) {
        vm.loadInitial()
    }

    // 2) Escucha eventos (SSE -> refreshFlow) y refresca la lista
    LaunchedEffect(Unit) {
        vm.refreshFlow.collectLatest {
            lazyItems.refresh()
        }
    }

    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        OutlinedTextField(
            value = vm.query,
            onValueChange = vm::onQueryChange,
            singleLine = true,
            label = { Text("Buscar por cliente…") },
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
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 84.dp)
                ) {
                    items(lazyItems.itemCount) { index ->
                        val act: ActivityHeader? = lazyItems[index]
                        if (act != null) {
                            AppCard(onClick = { onOpenEditor(act.id, false) }) {
                                Column(Modifier.padding(16.dp)) {
                                    Text("Código: ${act.id}", style = MaterialTheme.typography.titleMedium)
                                    Text("Cliente: ${act.clientNombre}")
                                    Text("Fecha: ${act.fechaCreacion ?: "-"}")
                                 //   Spacer(Modifier.height(8.dp))
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Guía: ${act.nroSerie}-${act.nroGuia}")
                                        Text("Peso total: ${act.totalPeso}")
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                        if (act.estado == 0) {
                                            TextButton(onClick = { onOpenEditor(act.id, true) }) { Text("Editar") }
                                            TextButton(onClick = {
                                                selectedId = act.id
                                                showConfirmDialog = true
                                            }) { Text("Almacén") }
                                        }
                                    }
                                }
                            }
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

    if (showConfirmDialog) {
        ConfirmDialog(
            show = showConfirmDialog,
            message = "¿Deseas realizar el ingreso almacén?",
            onConfirm = {
                selectedId?.let(onAlmacenConfirm)
                showConfirmDialog = false
                selectedId = null
            },
            onDismiss = {
                showConfirmDialog = false
                selectedId = null
            }
        )
    }
}
