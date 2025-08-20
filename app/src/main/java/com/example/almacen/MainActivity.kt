package com.example.almacen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.almacen.catalog.presentation.ArticleListActivity
import com.example.almacen.catalog.presentation.ClientListActivity
import com.example.almacen.catalog.presentation.ReasonListActivity
import com.example.almacen.catalog.presentation.StoreListActivity
import com.example.almacen.core.datastore.TokenStore
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_activity.presentation.ActivityListActivity
import com.example.almacen.feature_login.presentation.LoginActivity
import com.example.almacen.presentation.ui.MainMenuScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlmacenTheme {
                val ctx = LocalContext.current
                val username by TokenStore.usernameFlow(ctx)
                    .collectAsStateWithLifecycle(initialValue = null)
                val idUser by TokenStore.iduserFlow(ctx)
                    .collectAsStateWithLifecycle(initialValue = null)

                MainMenuScreen(
                    displayName = username ?: "Usuario",
                    onActividad = {
                        startActivity(Intent(this, ActivityListActivity::class.java))
                    },
                    onClientes   = {
                        startActivity(Intent(this, ClientListActivity::class.java)) },
                    onAlmacen    = {
                        startActivity(Intent(this, StoreListActivity::class.java))
                    },
                    onMotivos    = {
                        startActivity(Intent(this, ReasonListActivity::class.java))
                    },
                    onArticulos  = {
                        startActivity(Intent(this, ArticleListActivity::class.java))
                    },
                    onLogout = {
                        lifecycleScope.launch {
                            // Navega al login limpiando el back stack
                            val intent = Intent(this@MainActivity, LoginActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            startActivity(intent)
                        }
                    }
                )
            }
        }
    }
}
