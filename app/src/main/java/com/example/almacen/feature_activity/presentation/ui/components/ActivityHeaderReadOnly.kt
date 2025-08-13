package com.example.almacen.feature_activity.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// 1) Encabezado de solo lectura (ya con campos sueltos)
@Composable
fun ActivityHeaderReadOnly(
    id: Long?,
    nroSerie: String?,
    nroGuia: String?,
    fechaCreacion: String?,
    clientNombre: String?,
    storeNombre: String?,
    reasonNombre: String?,
    observacion: String?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Código: ${id ?: "-"}", style = MaterialTheme.typography.titleMedium)
        Text("Guía: ${nroSerie ?: "-"}-${nroGuia ?: "-"}")
    }
    Text("Fecha: ${fechaCreacion ?: "-"}")
    Text("Cliente: ${clientNombre ?: "-"}")
    Text("Almacén: ${storeNombre ?: "-"}")
    Text("Motivo: ${reasonNombre ?: "-"}")
    Text("Observación: ${observacion ?: "-"}")
}


