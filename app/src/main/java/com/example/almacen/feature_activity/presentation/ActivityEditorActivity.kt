package com.example.almacen.feature_activity.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_activity.presentation.state.NewActivityState
import com.example.almacen.feature_activity.presentation.ui.ActivityEditorScreen
import com.example.almacen.feature_activity.presentation.viewmodel.ActivityEditorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityEditorActivity : ComponentActivity() {

    private val vm: ActivityEditorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idFromIntent: Int? = extractActivityId()
        val startInEdit: Boolean = intent?.getBooleanExtra("startInEdit", false) ?: false

        vm.initIfNeeded(idFromIntent)

        // Aplica el modo edición ANTES de componer (una sola vez)
        if (startInEdit)
            vm.enterEdit()
         else
            vm.enterView(showEdit = false)

        setContent {
            AlmacenTheme {
                val state: NewActivityState by vm.state.collectAsState()

                // Cierre al guardar
                LaunchedEffect(state.savedId) {
                    state.savedId?.let {
                        setResult(Activity.RESULT_OK, Intent().putExtra("savedId", it))
                        vm.consumeSavedEvent()
                        finish()
                    }
                }

                ActivityEditorScreen(
                    onBack = { finish() },
                    vm = vm
                )
            }
        }
    }

    private fun extractActivityId(): Int? {
        val extras = intent?.extras ?: return null
        val any = if (extras.containsKey("activityId")) extras.get("activityId") else null
        val id = when (any) {
            is Int -> any
            is Long -> any.takeIf { it <= Int.MAX_VALUE }?.toInt()
            is String -> any.toIntOrNull()
            else -> null
        }
        return id?.takeIf { it > 0 }
    }
}
