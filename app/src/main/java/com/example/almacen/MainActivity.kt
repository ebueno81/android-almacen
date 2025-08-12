package com.example.almacen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.almacen.core.datastore.TokenStore
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_activity.presentation.ActivityListActivity
import com.example.almacen.presentation.ui.MainMenuScreen

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
                    onClientes   = { /* TODO: navegar a feature_client */ },
                    onAlmacen    = { /* TODO: navegar a feature_store */ },
                    onUsuario    = { /* TODO: navegar a perfil/usuario */ },
                    onArticulos  = { /* TODO: navegar a feature_article */ }
                )
            }
        }
    }
}
