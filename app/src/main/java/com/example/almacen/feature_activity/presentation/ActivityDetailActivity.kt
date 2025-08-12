package com.example.almacen.feature_activity.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_activity.presentation.ui.ActivityDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityId = intent.getLongExtra("activityId", 0L)

        setContent {
            AlmacenTheme {
                ActivityDetailScreen(
                    activityId = activityId
                )
            }
        }
    }
}
