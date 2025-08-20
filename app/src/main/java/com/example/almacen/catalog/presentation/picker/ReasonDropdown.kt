package com.example.almacen.catalog.presentation.picker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.almacen.catalog.domain.model.Reason

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReasonDropdown(
    reasons: List<Reason>,
    selected: Reason?,
    onSelected: (Reason) -> Unit,
    enabled: Boolean = true,
    label: String = "Motivo"
) {
    var expanded by remember { mutableStateOf(false) }
    val hasData = reasons.isNotEmpty()
    val actuallyEnabled = enabled && hasData

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (actuallyEnabled) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected?.nombre ?: "",   // ⬅️ si tu modelo usa otra prop (p.ej. descripcion), cámbiala aquí
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            enabled = enabled,                // solo pinta colores; la apertura la controla actuallyEnabled
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded && hasData,
            onDismissRequest = { expanded = false }
        ) {
            reasons.forEach { r ->
                DropdownMenuItem(
                    text = { Text("${r.nombre} (${r.tipo})") }, // ⬅️ idem: cambia si tu campo visible no es 'nombre'
                    onClick = {
                        onSelected(r)
                        expanded = false
                    }
                )
            }
        }
    }
}
