package com.example.almacen.feature_activity.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 2) Card de detalles de solo lectura (GENÉRICO)
// T = el tipo real de los items de act.detalles (el que ya usas)
@Composable
fun <T> ActivityDetailsReadOnlyCard(
    detalles: List<T>,
    getLote: (T) -> String?,
    getArticuloId: (T) -> Any?,          // Int/Long/String: usamos Any?
    getNombreArticulo: (T) -> String?,
    getPeso: (T) -> Any?,                // Double/BigDecimal/etc.
    getCajas: (T) -> Any?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column {
            detalles.forEachIndexed { idx, det ->
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = buildString {
                        appendLine("N° Lote: ${getLote(det) ?: "-"}")
                        appendLine("Artículo: ${getArticuloId(det) ?: "-"} - ${getNombreArticulo(det) ?: "-"}")
                        append("Peso: ${getPeso(det) ?: "-"} | Cajas: ${getCajas(det) ?: "-"}")
                    }
                )
                if (idx < detalles.lastIndex) {
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
        }
    }
}

