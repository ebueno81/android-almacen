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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.almacen.core.ui.components.AppCard
import com.example.almacen.core.ui.components.AppScaffold
import com.example.almacen.feature_activity.presentation.viewmodel.ActivityEditorViewModel

@Composable
fun ActivityEditorScreen(
    onBack: () -> Unit,
    vm: ActivityEditorViewModel = hiltViewModel()
) {
    val ui by vm.state.collectAsState()

    // diálogos
    var showClientPicker by remember { mutableStateOf(false) }
    var showArticlePickerIndex by remember { mutableStateOf<Int?>(null) }

    val canSubmit =
        !vm.readOnly &&
                vm.selectedClient != null &&
                vm.selectedStore  != null &&
                vm.selectedReason != null &&
                vm.detalles.isNotEmpty() &&
                !ui.isLoading

    AppScaffold(
        title = when {
            vm.activityId == null -> "Nueva actividad"
            vm.readOnly          -> "Detalle de actividad"
            else                 -> "Editar actividad"
        },
        onBack = onBack,
        fab = {
            if (vm.readOnly) {
                ExtendedFloatingActionButton(
                    onClick = { vm.enterEdit() },
                    icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                    text = { Text("Editar") }
                )
            } else {
                ExtendedFloatingActionButton(
                    onClick = { if (canSubmit) vm.save() },
                    icon = { Icon(Icons.Filled.Check, contentDescription = null) },
                    text = { Text(if (ui.isLoading) "Guardando…" else "Guardar") }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Cliente (readonly + clic para abrir picker)
            item {
                OutlinedTextField(
                    value = vm.selectedClient?.nombre ?: "",
                    onValueChange = {},
                    label = { Text("Cliente") },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !vm.readOnly) { showClientPicker = true },
                    trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
                )
            }

            // Almacén
            item {
                StoreDropdown(
                    stores = vm.stores,
                    selected = vm.selectedStore,
                    onSelected = { if (!vm.readOnly) vm.selectStore(it) }
                )
            }

            // Motivo
            item {
                ReasonDropdown(
                    reasons = vm.reasons,
                    selected = vm.selectedReason,
                    onSelected = { if (!vm.readOnly) vm.selectReason(it) }
                )
            }

            // N° Guía
            item {
                OutlinedTextField(
                    value = vm.nroGuia,
                    onValueChange = { if (!vm.readOnly) vm.onNroGuiaChange(it) },
                    label = { Text("N° Guía") },
                    enabled = !vm.readOnly,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Serie
            item {
                OutlinedTextField(
                    value = vm.nroSerie,
                    onValueChange = { if (!vm.readOnly) vm.onNroSerieChange(it) },
                    label = { Text("Serie de Guía") },
                    enabled = !vm.readOnly,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Observaciones
            item {
                OutlinedTextField(
                    value = vm.observaciones,
                    onValueChange = { if (!vm.readOnly) vm.onObservacionesChange(it) },
                    label = { Text("Observaciones") },
                    enabled = !vm.readOnly,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }

            item { Divider() }
            item { Text("DETALLES", style = MaterialTheme.typography.titleMedium) }

            // Detalles
            items(vm.detalles.size) { idx ->
                val row = vm.detalles[idx]
                AppCard {
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
                                .clickable(enabled = !vm.readOnly) { showArticlePickerIndex = idx },
                            trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
                        )
                        OutlinedTextField(
                            value = row.lote,
                            onValueChange = { if (!vm.readOnly) vm.updateDetailRow(idx, row.copy(lote = it)) },
                            label = { Text("N° Lote") },
                            enabled = !vm.readOnly,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = row.peso,
                                onValueChange = { if (!vm.readOnly) vm.updateDetailRow(idx, row.copy(peso = it)) },
                                label = { Text("Peso") },
                                enabled = !vm.readOnly,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = row.cajas,
                                onValueChange = { if (!vm.readOnly) vm.updateDetailRow(idx, row.copy(cajas = it)) },
                                label = { Text("Cajas") },
                                enabled = !vm.readOnly,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (!vm.readOnly) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                TextButton(onClick = { vm.removeDetailRow(idx) }) { Text("Eliminar") }
                            }
                        }
                    }
                }
            }

            if (!vm.readOnly) {
                item {
                    OutlinedButton(
                        onClick = { vm.addDetailRow() },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Agregar detalle") }
                }
            }
        }
    }

    // ===== Diálogos =====
    if (showClientPicker && !vm.readOnly) {
        ClientPickerDialog(
            onDismiss = { showClientPicker = false },
            onSelected = { c -> vm.selectClient(c); showClientPicker = false },
            pagingFlow = vm.clientPagingFlow,
            onQueryChange = vm::onClientQueryChange
        )
    }

    val idx = showArticlePickerIndex
    if (idx != null && !vm.readOnly) {
        ArticlePickerDialog(
            onDismiss = { showArticlePickerIndex = null },
            articles = vm.articles,
            onSelected = { art ->
                vm.updateDetailRow(idx, vm.detalles[idx].copy(articulo = art))
                showArticlePickerIndex = null
            }
        )
    }
}

/* ---------- Diálogo local de artículos ---------- */
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
                    items(filtered) { a ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelected(a) }
                                .padding(vertical = 10.dp, horizontal = 8.dp)
                        ) {
                            Text(a.nombre)
                            Text("ID: ${a.id}")
                        }
                        Divider()
                    }
                }
            }
        }
    )
}
