package com.example.almacen.feature_activity.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.almacen.feature_activity.presentation.ui.components.ActivityDetailsReadOnlyCard
import com.example.almacen.feature_activity.presentation.ui.components.ActivityHeaderReadOnly
import com.example.almacen.feature_activity.presentation.viewmodel.ActivityDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    activityId: Long,
    vm: ActivityDetailViewModel = hiltViewModel()
) {
    val state = vm.state.collectAsState()

    LaunchedEffect(activityId) { vm.loadActivity(activityId) }

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
    ) { inner ->
        when {
            state.value.isLoading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(inner),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            state.value.error != null -> {
                Text(
                    text = "Error: ${state.value.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(inner)
                        .padding(16.dp)
                )
            }

            state.value.activity != null -> {
                val act = state.value.activity!!

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(inner),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        ActivityHeaderReadOnly(
                            id = act.id,
                            nroSerie = act.nroSerie,
                            nroGuia = act.nroGuia,
                            fechaCreacion = act.fechaCreacion,
                            clientNombre = act.clientNombre,
                            storeNombre = act.storeNombre,
                            reasonNombre = act.reasonNombre,
                            observacion = act.observacion
                        )

                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "DETALLES",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color(0xFF8BC34A))
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }

                    item {
                        ActivityDetailsReadOnlyCard(
                            detalles = act.detalles,
                            getLote = { it.lote },
                            getArticuloId = { it.articuloId },
                            getNombreArticulo = { it.nombreArticulo },
                            getPeso = { it.peso },
                            getCajas = { it.cajas }
                        )
                    }

                    item { Spacer(Modifier.height(12.dp)) }
                }
            }
        }
    }
}