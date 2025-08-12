package com.example.almacen.feature_activity.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.almacen.feature_activity.presentation.viewmodel.ActivityListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityListScreen(
    onActivityClick: (Long) -> Unit,
    vm: ActivityListViewModel = hiltViewModel()
) {
    val state = vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadActivities()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Actividades") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary
                ))
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
            else -> {
                Column(Modifier.padding(padding)) {
                    state.value.activities.forEach { act ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { onActivityClick(act.id) },
                            colors = androidx.compose.material3.CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,   // Fondo según el tema
                                contentColor = MaterialTheme.colorScheme.onSurface    // Texto según el tema
                            ),
                            elevation = androidx.compose.material3.CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    text = "Codigo: ${act.id}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Cliente: ${act.clientNombre}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Fecha: ${act.fechaCreacion ?: "-"}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Row (modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Text(
                                        text = "Guía: ${act.nroSerie + "-" + act.nroGuia}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Peso total: ${act.totalPeso}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
