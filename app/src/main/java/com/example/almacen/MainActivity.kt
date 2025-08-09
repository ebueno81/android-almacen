package com.example.almacen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.almacen.core.ui.theme.AlmacenTheme   // ← usa tu theme global (ajusta el import)
import com.example.almacen.presentation.ui.MainMenuScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlmacenTheme {
                MainMenuScreen(
                    onActividad = { /* TODO: navegar a feature_activity */ },
                    onClientes   = { /* TODO: navegar a feature_client */ },
                    onAlmacen    = { /* TODO: navegar a feature_store */ },
                    onUsuario    = { /* TODO: navegar a perfil/usuario */ },
                    onArticulos  = { /* TODO: navegar a feature_article */ }
                )
            }
        }
    }
}
