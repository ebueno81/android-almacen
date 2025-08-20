package com.example.almacen.catalog.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.example.almacen.catalog.presentation.screen.ArticleListScreen
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_activity.presentation.ActivityListActivity
import com.example.almacen.presentation.ui.component.AppScaffold
import com.example.almacen.presentation.ui.component.MainTab
import com.example.almacen.util.goHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlmacenTheme {
                AppScaffold(
                    title = "Artículos",
                    subtitle = "Catálogo",
                    currentTab = MainTab.Articulos,
                    onLogout   = { /* TODO logout */ },
                    onHome = { goHome()  },
                    onActividad= { startActivity(Intent(this, ActivityListActivity::class.java)) },
                    onClientes = { startActivity(Intent(this, ClientListActivity::class.java)) },
                    onAlmacen  = { startActivity(Intent(this, StoreListActivity::class.java)) },
                    onArticulos= { /* stay */ },
                    onBack     = { finish() }   // flecha atrás
                ) { inner ->
                    ArticleListScreen(modifier = Modifier.padding(inner))
                }
            }
        }
    }
}
