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
import com.example.almacen.catalog.domain.model.Store

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDropdown(
    stores: List<Store>,
    selected: Store?,
    onSelected: (Store) -> Unit,
    enabled: Boolean = true,
    label: String = "Almacén"
) {
    var expanded by remember { mutableStateOf(false) }
    val hasData = stores.isNotEmpty()
    val actuallyEnabled = enabled && hasData

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (actuallyEnabled) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected?.nombre ?: "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,                                 // no teclado
            enabled = enabled,                               // colores según modo
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .menuAnchor()                                // ancla el menú
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded && hasData,                  // no abras si no hay data
            onDismissRequest = { expanded = false }
        ) {
            stores.forEach { s ->
                DropdownMenuItem(
                    text = { Text(s.nombre) },
                    onClick = {
                        onSelected(s)
                        expanded = false
                    }
                )
            }
        }
    }
}
