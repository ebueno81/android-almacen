package com.example.almacen.feature_activity.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_activity.presentation.ui.ActivityListScreen
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
                ActivityListScreen(
                    onActivityClick = { id ->
                        // Conserva el tipo original (probablemente Int), clave "activityId"
                        editorLauncher.launch(
                            Intent(this, ActivityEditorActivity::class.java)
                                .putExtra("activityId", id)
                        )
                    },
                    onCreateNew = {
                        editorLauncher.launch(Intent(this, ActivityEditorActivity::class.java))
                    }
                )
            }
        }
    }
}
