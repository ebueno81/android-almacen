package com.example.almacen.presentation.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable


@Composable
fun AppScaffold(
    title: String,
    subtitle: String = "",
    currentTab: MainTab?,
    onLogout: () -> Unit,
    onHome: () -> Unit,
    onActividad: () -> Unit,
    onClientes: () -> Unit,
    onAlmacen: () -> Unit,
    onArticulos: () -> Unit,
    onBack: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            MainHeader(
                title = title,
                subtitle = subtitle,
                onNotifications = { /* TODO */ },
                onLogout = onLogout,
                onBack = onBack
            )
        },
        bottomBar = {
            MainBottomBar(
                current = currentTab,
                onHome = onHome,
                onActividad = onActividad,
                onClientes = onClientes,
                onAlmacen = onAlmacen,
                onArticulos = onArticulos
            )
        }
    ) { inner -> content(inner) }
}
