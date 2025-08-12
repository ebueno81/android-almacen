package com.example.almacen.feature_activity.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.almacen.feature_activity.presentation.viewmodel.ActivityDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    activityId: Long,
    vm: ActivityDetailViewModel = hiltViewModel()
) {
    val state = vm.state.collectAsState()

    LaunchedEffect(activityId) {
        vm.loadActivity(activityId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle Actividad") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        when {
            state.value.isLoading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.value.error != null -> {
                Text(
                    text = "Error: ${state.value.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            state.value.activity != null -> {
                val act = state.value.activity!!
                Column(Modifier.padding(16.dp)) {
                    Text("Codigo: ${act.id}")
                    Text("Cliente: ${act.clientNombre}")
                    Text("Guía: ${act.nroSerie + "-" + act.nroGuia}")
                    Text("Fecha: ${act.fechaCreacion?: "-"}")
                    Text("Observación: ${act.observacion ?: "-"}")
                    Spacer(Modifier.height(12.dp))
                    Text("Detalles:")
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(1.dp),
                        colors = androidx.compose.material3.CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,   // Fondo según el tema
                            contentColor = MaterialTheme.colorScheme.onSurface    // Texto según el tema
                        ),
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    ){
                        act.detalles.forEach { det ->
                            Text("N°Lote: ${det.lote}")
                            Text("Artículo: ${det.articuloId} - ${det.nombreArticulo}")
                            Text("Peso: ${det.peso} | Cajas: ${det.cajas}")
                        }
                    }
                }
            }
        }
    }
}
