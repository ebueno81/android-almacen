package com.example.almacen.presentation.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.almacen.feature_activity.presentation.ActivityListActivity
import com.example.almacen.presentation.ui.component.MainBottomBar
import com.example.almacen.presentation.ui.component.MainHeader
import com.example.almacen.presentation.ui.component.QuickActionsGrid
import com.example.almacen.presentation.ui.component.QuickItem
import com.example.almacen.presentation.ui.component.SearchBlock
import com.example.almacen.presentation.ui.component.SectionTitle

@Composable
fun MainMenuScreen(
    displayName: String,
    onActividad: () -> Unit,
    onClientes: () -> Unit,
    onAlmacen: () -> Unit,
    onUsuario: () -> Unit,
    onArticulos: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val context = LocalContext.current

    Scaffold(
        topBar = {
            MainHeader(
                title = "Hola, $displayName",
                subtitle = "Bienvenido",
                onNotifications = { /* TODO */ }
            )
        },
        bottomBar = {
            MainBottomBar(
                onHome = { /* stay */ },
                onActividad = {
                    context.startActivity(
                        Intent(context, ActivityListActivity::class.java)
                    )
                },
                onClientes = onClientes,
                onAlmacen = onAlmacen,
                onUsuario = onUsuario
            )
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Bloque de búsqueda (opcional)
            SearchBlock(
                hint = "Buscar...",
                onSearch = { /* TODO */ }
            )

            Spacer(Modifier.height(16.dp))

            // Accesos rápidos (tarjetas)
            QuickActionsGrid(
                items = listOf(
                    QuickItem("Actividad", Icons.Filled.Assignment){
                        context.startActivity(
                            Intent(context, ActivityListActivity::class.java)
                        )
                    },
                    QuickItem("Clientes",  Icons.Filled.Groups,     onClientes),
                    QuickItem("Almacén",   Icons.Filled.Store,      onAlmacen),   // ← en vez de Warehouse
                    QuickItem("Usuario",   Icons.Filled.Person,     onUsuario),
                    QuickItem("Artículos", Icons.Filled.Inventory,  onArticulos), // ← en vez de Inventory2
                )
            )
            Spacer(Modifier.height(24.dp))

            // Lista o secciones extra (placeholder)
            SectionTitle("Servicios")
            repeat(10) { i ->
                ListItem(
                    headlineContent = { Text("Item $i") }
                )
                Divider()
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}