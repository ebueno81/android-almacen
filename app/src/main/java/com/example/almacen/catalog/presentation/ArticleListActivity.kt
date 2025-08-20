package com.example.almacen.catalog.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.almacen.catalog.presentation.screen.ArticleListScreen
import com.example.almacen.core.ui.theme.AlmacenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlmacenTheme {
                ArticleListScreen(onBack = { finish() })
            }
        }
    }
}
