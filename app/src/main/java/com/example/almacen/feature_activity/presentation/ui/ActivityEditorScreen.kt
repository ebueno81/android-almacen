package com.example.almacen.feature_activity.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.almacen.catalog.presentation.picker.ArticlePickerDialog
import com.example.almacen.catalog.presentation.picker.ClientPickerDialog
import com.example.almacen.catalog.presentation.picker.ReasonDropdown
import com.example.almacen.catalog.presentation.picker.StoreDropdown
import com.example.almacen.core.common.validators.isDecimal
import com.example.almacen.core.common.validators.isNumeric
import com.example.almacen.core.ui.components.AppCard
import com.example.almacen.core.ui.components.AppScaffold
import com.example.almacen.feature_activity.presentation.viewmodel.ActivityEditorViewModel

@Composable
fun ActivityEditorScreen(
    onBack: () -> Unit,
    startInEdit: Boolean = false,
    initialActivityId: Int? = null,
    vm: ActivityEditorViewModel = hiltViewModel()
) {
    // Inicialización 1 sola vez
    LaunchedEffect(Unit) {
        vm.initIfNeeded(initialActivityId)
    }

    val ui by vm.state.collectAsState()

    ui.error?.let { err ->
        AlertDialog(
            onDismissRequest = { vm.clearError() },
            confirmButton = {
                TextButton(onClick = { vm.clearError() }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Error") },
            text = { Text(err) }
        )
    }

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
            when {
                vm.readOnly && vm.showEditFab -> {
                    // FAB "Editar" visible SOLO en modo ver cuando así lo quieras
                    ExtendedFloatingActionButton(
                        onClick = { vm.enterEdit() },
                        icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                        text  = { Text("Editar") }
                    )
                }
                !vm.readOnly -> {
                    // En edición, mostrar solo "Guardar"
                    ExtendedFloatingActionButton(
                        onClick = { if (canSubmit) vm.save() },
                        icon = { Icon(Icons.Filled.Check, contentDescription = null) },
                        text  = { Text(if (ui.isLoading) "Guardando…" else "Guardar") }
                    )
                }
                else -> {
                    // Modo ver y showEditFab = false ⇒ no muestres FAB
                }
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

            item {
                StoreDropdown(
                    stores = vm.stores,
                    selected = vm.selectedStore,
                    onSelected = { if (!vm.readOnly) vm.selectStore(it) }
                )
            }

            item {
                ReasonDropdown(
                    reasons = vm.reasons,
                    selected = vm.selectedReason,
                    onSelected = { if (!vm.readOnly) vm.selectReason(it) }
                )
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = vm.nroSerie,
                        onValueChange = { if (!vm.readOnly) vm.onNroSerieChange(it) },
                        label = { Text("Serie de Guía") },
                        enabled = !vm.readOnly,
                        modifier = Modifier.weight(0.5f) // cada campo ocupa mitad
                    )
                    OutlinedTextField(
                        value = vm.nroGuia,
                        onValueChange = {input ->
                            if(input.isNumeric())
                                if (!vm.readOnly) vm.onNroGuiaChange(input)
                                        },
                        label = { Text("N° Guía") },
                        enabled = !vm.readOnly,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(0.5f)
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = vm.observaciones,
                    onValueChange = { if (!vm.readOnly) vm.onObservacionesChange(it) },
                    label = { Text("Observaciones") },
                    enabled = !vm.readOnly,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 1
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

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = row.lote,
                                onValueChange = { if (!vm.readOnly) vm.updateDetailRow(idx, row.copy(lote = it)) },
                                label = { Text("N° Lote") },
                                enabled = !vm.readOnly,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Characters
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = row.peso,
                                onValueChange = {input->
                                    if(!vm.readOnly && input.isDecimal())
                                        vm.updateDetailRow(idx,row.copy(peso=input))
                                },
                                label = { Text("Peso") },
                                enabled = !vm.readOnly,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = row.cajas,
                                onValueChange = {input->
                                    if (!vm.readOnly && input.isNumeric()){
                                        vm.updateDetailRow(idx,row.copy(cajas = input))
                                    }
                                                },
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
            pagingFlow = vm.articlePagingFlow,        // ✅ usar el Flow
            onQueryChange = vm::onArticleQueryChange, // ✅ igual que clientes
            onSelected = { art ->
                vm.updateDetailRow(idx, vm.detalles[idx].copy(articulo = art))
                showArticlePickerIndex = null
            }
        )
    }
}