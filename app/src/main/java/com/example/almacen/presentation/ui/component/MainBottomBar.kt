package com.example.almacen.presentation.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MainBottomBar(
    current: MainTab,                // 👈 NUEVO
    onHome: () -> Unit,
    onActividad: () -> Unit,
    onClientes: () -> Unit,
    onAlmacen: () -> Unit,
    onUsuario: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = current == MainTab.Home,
            onClick = onHome,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = current == MainTab.Actividad,
            onClick = onActividad,
            icon = { Icon(Icons.Default.Assignment, contentDescription = "Actividad") },
            label = { Text("Actividad") }
        )
        NavigationBarItem(
            selected = current == MainTab.Clientes,
            onClick = onClientes,
            icon = { Icon(Icons.Default.Groups, contentDescription = "Clientes") },
            label = { Text("Clientes") }
        )
        NavigationBarItem(
            selected = current == MainTab.Almacen,
            onClick = onAlmacen,
            icon = { Icon(Icons.Default.Warehouse, contentDescription = "Almacén") },
            label = { Text("Almacén") }
        )
        NavigationBarItem(
            selected = current == MainTab.Usuario,
            onClick = onUsuario,
            icon = { Icon(Icons.Default.Person, contentDescription = "Usuario") },
            label = { Text("Usuario") }
        )
    }
}
