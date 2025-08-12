package com.example.almacen.feature_activity.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_activity.presentation.ui.ActivityListScreen

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlmacenTheme {
                ActivityListScreen(
                    onActivityClick = { id ->
                        startActivity(
                            Intent(this, ActivityDetailActivity::class.java)
                                .putExtra("activityId", id)
                        )
                    }
                )
            }
        }
    }
}