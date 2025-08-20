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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.almacen.MainActivity
import com.example.almacen.core.ui.components.AppCard
import com.example.almacen.core.ui.components.AppScaffold
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
    val state by vm.state.collectAsState()
    val ctx = LocalContext.current
    val activity = ctx as? Activity

    var showConfirmDialog by remember { mutableStateOf(false) }
    var activityIdToCancel by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) { vm.loadActivities() }

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
                    // (opcional) si quieres forzar volver al Main y limpiar la pila:
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
        when {
            state.isLoading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) { CircularProgressIndicator() }

            state.error != null -> Text(
                text = "Error: ${state.error}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(state.headers) { act ->
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
                                TextButton(onClick = { onOpenEditor(act.id, true) }) { Text("Editar") }
                                TextButton(onClick = {
                                    activityIdToCancel = act.id.toLong()
                                    showConfirmDialog = true
                                }) { Text("Anular") }
                            }
                        }
                    }
                }
            }
        }
    }

    ConfirmDialog(
        show = showConfirmDialog,
        message = "¿Desea anular el registro?",
        onConfirm = {
            activityIdToCancel?.let { id ->
                // vm.cancelActivity(id) si ya lo tienes
                println("Anulando actividad $id")
            }
            showConfirmDialog = false
        },
        onDismiss = { showConfirmDialog = false }
    )
}
