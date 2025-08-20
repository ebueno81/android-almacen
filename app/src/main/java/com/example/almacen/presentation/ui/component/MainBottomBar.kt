package com.example.almacen.presentation.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MainBottomBar(
    current: MainTab?,
    onHome: () -> Unit,
    onActividad: () -> Unit,
    onClientes: () -> Unit,
    onAlmacen: () -> Unit,
    onArticulos: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = current == MainTab.Home,
            onClick = onHome,
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = current == MainTab.Actividad,
            onClick = onActividad,
            icon = { Icon(Icons.Filled.Assignment, contentDescription = "Actividad") },
            label = { Text("Actividad") }
        )
        NavigationBarItem(
            selected = current == MainTab.Clientes,
            onClick = onClientes,
            icon = { Icon(Icons.Filled.Groups, contentDescription = "Clientes") },
            label = { Text("Clientes") }
        )
        NavigationBarItem(
            selected = current == MainTab.Almacen,
            onClick = onAlmacen,
            icon = { Icon(Icons.Filled.Store, contentDescription = "Almacén") },
            label = { Text("Almacén") }
        )
        NavigationBarItem(
            selected = current == MainTab.Articulos,
            onClick = onArticulos,
            icon = { Icon(Icons.Filled.Inventory, contentDescription = "Artículos") },
            label = { Text("Artículos") }
        )
    }
}

