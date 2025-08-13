package com.example.almacen.feature_activity.presentation.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.almacen.catalog.domain.model.Article
import com.example.almacen.catalog.presentation.picker.ClientPickerDialog
import com.example.almacen.catalog.presentation.picker.ReasonDropdown
import com.example.almacen.catalog.presentation.picker.StoreDropdown
import com.example.almacen.feature_activity.presentation.viewmodel.NewActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewActivityScreen(
    onBack: () -> Unit,
    onCreated: (Long) -> Unit,
    vm: NewActivityViewModel = hiltViewModel()
) {
    // Carga catálogos al entrar
    LaunchedEffect(Unit) { vm.loadStaticLists() }

    // Navegar cuando se cree
    val uiState by vm.state.collectAsState()
    LaunchedEffect(uiState.createdActivity?.id) {
        uiState.createdActivity?.id?.let(onCreated)
    }

    var showClientPicker by remember { mutableStateOf(false) }
    var showArticlePickerIndex by remember { mutableStateOf<Int?>(null) }

    val canSubmit =
        vm.selectedClient != null &&
                vm.selectedStore  != null &&
                vm.selectedReason != null &&
                vm.detalles.isNotEmpty()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nueva actividad") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(if (uiState.isLoading) "Guardando…" else "Guardar") },
                icon = { Icon(Icons.Filled.Check, contentDescription = null) },
                onClick = { if (!uiState.isLoading && canSubmit) vm.createFromState() }
            )
        }
    ) { innerPadding: PaddingValues ->

        // ======= CONTENIDO =======
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Cliente
            item {
                OutlinedTextField(
                    value = vm.selectedClient?.nombre ?: "",
                    onValueChange = {},
                    label = { Text("Cliente") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showClientPicker = true },
                    enabled = false,
                    trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
                )
            }

            // Almacén
            item {
                StoreDropdown(
                    stores = vm.stores,
                    selected = vm.selectedStore,
                    onSelected = vm::selectStore
                )
            }

            // Motivo
            item {
                ReasonDropdown(
                    reasons = vm.reasons,
                    selected = vm.selectedReason,
                    onSelected = vm::selectReason
                )
            }

            // N° Guía
            item {
                OutlinedTextField(
                    value = vm.nroGuia,
                    onValueChange = vm::onNroGuiaChange,
                    label = { Text("N° Guía") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            // Serie de Guía
            item {
                OutlinedTextField(
                    value = vm.serieGuia,
                    onValueChange = vm::onSerieGuiaChange,
                    label = { Text("Serie de Guía") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Observaciones
            item {
                OutlinedTextField(
                    value = vm.observaciones,
                    onValueChange = vm::onObservacionesChange,
                    label = { Text("Observaciones") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }

            item { Divider() }
            item { Text("DETALLES") }

            // Detalle
            items(vm.detalles.size) { idx ->
                val row = vm.detalles[idx]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = row.articulo?.nombre ?: "",
                            onValueChange = {},
                            label = { Text("Artículo") },
                            enabled = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showArticlePickerIndex = idx },
                            trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
                        )
                        OutlinedTextField(
                            value = row.lote,
                            onValueChange = { vm.updateDetailRow(idx, row.copy(lote = it)) },
                            label = { Text("N° Lote") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = row.peso,
                                onValueChange = { vm.updateDetailRow(idx, row.copy(peso = it)) },
                                label = { Text("Peso") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            OutlinedTextField(
                                value = row.cajas,
                                onValueChange = { vm.updateDetailRow(idx, row.copy(cajas = it)) },
                                label = { Text("Cajas") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { vm.removeDetailRow(idx) }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = { vm.addDetailRow() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Agregar detalle") }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
        // ======= FIN CONTENIDO =======
    } // <-- cierre correcto del Scaffold

    // ======= DIÁLOGOS =======

    // Diálogo de clientes (paginado)
    if (showClientPicker) {
        ClientPickerDialog(
            onDismiss = { showClientPicker = false },
            onSelected = { c ->
                vm.selectClient(c)
                showClientPicker = false
            },
            pagingFlow = vm.clientPagingFlow,
            onQueryChange = vm::onClientQueryChange // nombre nuevo para evitar choque de setter
        )
    }

    // Diálogo simple de artículos (lista local)
    val idx = showArticlePickerIndex
    if (idx != null) {
        ArticlePickerDialog(
            onDismiss = { showArticlePickerIndex = null },
            articles = vm.articles,
            onSelected = { art ->
                val row = vm.detalles[idx]
                vm.updateDetailRow(idx, row.copy(articulo = art))
                showArticlePickerIndex = null
            }
        )
    }
    // ======= FIN DIÁLOGOS =======
}

/* ---------- Diálogo local para artículos (sin paging) ---------- */
@Composable
private fun ArticlePickerDialog(
    onDismiss: () -> Unit,
    articles: List<Article>,
    onSelected: (Article) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(articles, query) {
        if (query.isBlank()) articles else articles.filter { it.nombre.contains(query, true) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Seleccionar artículo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar…") },
                    singleLine = true,
                    trailingIcon = { Icon(Icons.Filled.Search, null) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(4.dp))
                LazyColumn(modifier = Modifier.height(360.dp)) {
                    items(filtered.size) { i ->
                        val a = filtered[i]
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelected(a) }
                                .padding(vertical = 10.dp, horizontal = 8.dp)
                        ) {
                            Text(a.nombre)
                            Text("ID: ${a.id}")
                        }
                        if (i < filtered.lastIndex) Divider()
                    }
                }
            }
        }
    )
}