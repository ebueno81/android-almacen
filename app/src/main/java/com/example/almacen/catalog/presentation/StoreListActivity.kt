package com.example.almacen.catalog.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.example.almacen.catalog.presentation.screen.StoreListScreen
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_activity.presentation.ActivityListActivity
import com.example.almacen.presentation.ui.component.AppScaffold
import com.example.almacen.presentation.ui.component.MainTab
import com.example.almacen.util.goHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Usa tu theme para mantener el color verde
            AlmacenTheme {
                AppScaffold(
                    title = "Almacenes",
                    subtitle = "Catálogo",
                    currentTab = MainTab.Almacen,
                    onLogout   = { /* TODO logout */ },
                    onHome = { goHome()  },
                    onActividad= { startActivity(Intent(this, ActivityListActivity::class.java)) },
                    onClientes = { startActivity(Intent(this, ClientListActivity::class.java)) },
                    onAlmacen  = { /* stay */ },
                    onArticulos= { startActivity(Intent(this, ArticleListActivity::class.java)) },
                    onBack     = { finish() } // flecha atrás
                ) { inner ->
                    StoreListScreen(modifier = Modifier.padding(inner))
                }
            }
        }
    }
}
