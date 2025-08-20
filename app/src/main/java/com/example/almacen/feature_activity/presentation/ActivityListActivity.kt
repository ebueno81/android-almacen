// com.example.almacen.feature_activity.presentation/ActivityListActivity.kt
package com.example.almacen.feature_activity.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import com.example.almacen.catalog.presentation.ArticleListActivity
import com.example.almacen.catalog.presentation.ClientListActivity
import com.example.almacen.catalog.presentation.StoreListActivity
import com.example.almacen.core.ui.components.AppScaffold
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_activity.presentation.ui.ActivityListScreen
import com.example.almacen.presentation.ui.component.MainBottomBar
import com.example.almacen.presentation.ui.component.MainTab
import com.example.almacen.util.goHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityListActivity : ComponentActivity() {

    private val editorLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            recreate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlmacenTheme {
                AppScaffold(
                    title = "Actividades",
                    onBack = { finish() },
                    fab = {
                        FloatingActionButton(
                            onClick = {
                                editorLauncher.launch(
                                    Intent(this, ActivityEditorActivity::class.java)
                                        .putExtra("startInEdit", true)
                                )
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Nueva")
                        }
                    },
                    bottomBar = {
                        MainBottomBar(
                            current = MainTab.Actividad,
                            onHome = { goHome()  },
                            onActividad = { /* stay */ },
                            onClientes  = { startActivity(Intent(this, ClientListActivity::class.java)) },
                            onAlmacen   = { startActivity(Intent(this, StoreListActivity::class.java)) },
                            onArticulos = { startActivity(Intent(this, ArticleListActivity::class.java)) }
                        )
                    }
                ) { inner ->
                    ActivityListScreen(
                        modifier = Modifier.padding(inner),
                        onOpenEditor = { id, startInEdit ->
                            editorLauncher.launch(
                                Intent(this, ActivityEditorActivity::class.java)
                                    .putExtra("activityId", id)
                                    .putExtra("startInEdit", startInEdit)
                            )
                        },
                        onCreateNew = {
                            editorLauncher.launch(
                                Intent(this, ActivityEditorActivity::class.java)
                                    .putExtra("startInEdit", true)
                            )
                        },
                        onAlmacenConfirm = { id ->           // 👈 AHORA SÍ lo pasamos
                            // TODO: tu acción real. Ejemplos:
                            // startActivity(Intent(this, AlmacenIngresoActivity::class.java).putExtra("activityId", id))
                            Toast.makeText(this, "Ingreso a almacén $id", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
