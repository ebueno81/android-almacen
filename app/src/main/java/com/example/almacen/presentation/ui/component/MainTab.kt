package com.example.almacen.presentation.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainTab(val label: String, val icon: ImageVector) {
    Home("Inicio", Icons.Filled.Home),
    Actividad("Actividad", Icons.Filled.Assignment),
    Clientes("Clientes", Icons.Filled.Groups),
    Almacen("Almacén", Icons.Filled.Store),
    Articulos("Articulos", Icons.Filled.Inventory)
}
