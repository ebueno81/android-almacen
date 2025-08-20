package com.example.almacen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.almacen.catalog.presentation.ArticleListActivity
import com.example.almacen.catalog.presentation.ClientListActivity
import com.example.almacen.catalog.presentation.ReasonListActivity
import com.example.almacen.catalog.presentation.StoreListActivity
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
                    }
                )
            }
        }
    }
}
