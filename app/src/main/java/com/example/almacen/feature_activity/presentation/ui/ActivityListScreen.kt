package com.example.almacen.feature_activity.presentation.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.almacen.MainActivity
import com.example.almacen.core.ui.components.AppCard
import com.example.almacen.core.ui.components.AppScaffold
import com.example.almacen.feature_activity.domain.model.ActivityHeader
import com.example.almacen.feature_activity.presentation.ui.components.ConfirmDialog
import com.example.almacen.feature_activity.presentation.viewmodel.ActivityListViewModel
import com.example.almacen.presentation.ui.component.MainBottomBar
import com.example.almacen.presentation.ui.component.MainTab

@Composable
fun ActivityListScreen(
    onOpenEditor: (Int, Boolean) -> Unit,
    onCreateNew: () -> Unit,
    vm: ActivityListViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    val activity = ctx as? Activity

    // Carga inicial
    LaunchedEffect(Unit) { vm.loadInitial() }

    val lazyItems = vm.pagingFlow.collectAsLazyPagingItems()

    // 🔸 Estados para el diálogo de anulación
    var showConfirmDialog by remember { mutableStateOf(false) }
    var activityIdToCancel by remember { mutableStateOf<Int?>(null) }

    AppScaffold(
        title = "Actividades",
        onBack = { activity?.finish() },
        fab = {
            FloatingActionButton(onClick = onCreateNew) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo")
            }
        },
        bottomBar = {
            MainBottomBar(
                current = MainTab.Actividad,
                onHome = {
                    ctx.startActivity(
                        Intent(ctx, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    )
                    activity?.finish()
                },
                onActividad = { /* ya estás aquí */ },
                onClientes = { /* TODO */ },
                onAlmacen = { /* TODO */ },
                onMotivos = { /* TODO */ }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
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
                is LoadState.Loading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) { CircularProgressIndicator() }

                is LoadState.Error -> Text(
                    text = "Error: ${st.error.message}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 84.dp)
                    ) {
                        items(count = lazyItems.itemCount) { index ->
                            val act: ActivityHeader? = lazyItems[index]
                            if (act != null) {
                                AppCard(onClick = { onOpenEditor(act.id, false) }) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text("Código: ${act.id}", style = MaterialTheme.typography.titleMedium)
                                        Text("Cliente: ${act.clientNombre}")
                                        Text("Fecha: ${act.fechaCreacion ?: "-"}")
                                        Spacer(Modifier.height(8.dp))
                                        Row(
                                            Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Guía: ${act.nroSerie}-${act.nroGuia}")
                                            Text("Peso total: ${act.totalPeso}")
                                        }
                                        Spacer(Modifier.height(8.dp))
                                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                            TextButton(onClick = { onOpenEditor(act.id, true) }) {
                                                Text("Editar")
                                            }
                                            TextButton(
                                                onClick = {
                                                    // 🔸 Abre diálogo y guarda el ID a anular
                                                    activityIdToCancel = act.id
                                                    showConfirmDialog = true
                                                }
                                            ) {
                                                Text("Anular")
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        when (val ap = lazyItems.loadState.append) {
                            is LoadState.Loading -> item { Text("Cargando más…", Modifier.padding(12.dp)) }
                            is LoadState.Error -> item {
                                Text(
                                    "Error al cargar más: ${ap.error.message}",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    // 🔸 ConfirmDialog conectado
    ConfirmDialog(
        show = showConfirmDialog,
        message = "¿Desea anular el registro?",
        onConfirm = {
            activityIdToCancel?.let { id ->
                // TODO: llama a tu VM si tienes lógica de anulación:
                // vm.cancelActivity(id)
                println("Anulando actividad $id")
            }
            showConfirmDialog = false
            activityIdToCancel = null
        },
        onDismiss = {
            showConfirmDialog = false
            activityIdToCancel = null
        }
    )
}
