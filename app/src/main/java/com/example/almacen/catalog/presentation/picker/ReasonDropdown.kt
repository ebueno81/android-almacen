package com.example.almacen.catalog.presentation.picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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

@Composable
fun ReasonDropdown(
    reasons: List<Reason>,
    selected: Reason?,
    onSelected: (Reason) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = selected?.nombre ?: "",
        onValueChange = {},
        label = { Text("Motivo de ingreso") },
        modifier = Modifier.fillMaxWidth().clickable { expanded = true },
        enabled = false,
        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
    )
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        reasons.forEach { r ->
            DropdownMenuItem(
                text = { Text("${r.nombre} (${r.tipo})") },
                onClick = { onSelected(r); expanded = false }
            )
        }
    }
}
