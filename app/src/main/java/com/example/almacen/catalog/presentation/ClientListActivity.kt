package com.example.almacen.catalog.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.example.almacen.catalog.presentation.screen.ClientListScreen
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_activity.presentation.ActivityListActivity
import com.example.almacen.presentation.ui.component.AppScaffold
import com.example.almacen.presentation.ui.component.MainTab
import com.example.almacen.util.goHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 👇 Usa tu theme real (ej. AlmacenTheme / AppTheme)
            AlmacenTheme {
                AppScaffold(
                    title = "Clientes",
                    subtitle = "Catálogo",
                    currentTab = MainTab.Clientes,
                    onLogout   = { /* TODO logout */ },
                    onHome = { goHome()  },
                    onActividad= { startActivity(Intent(this, ActivityListActivity::class.java)) },
                    onClientes = { /* stay */ },
                    onAlmacen  = { startActivity(Intent(this, StoreListActivity::class.java)) },
                    onArticulos= { startActivity(Intent(this, ArticleListActivity::class.java)) },
                    onBack     = { finish() }
                ) { inner ->
                    ClientListScreen(modifier = Modifier.padding(inner))
                }
            }
        }
    }
}


