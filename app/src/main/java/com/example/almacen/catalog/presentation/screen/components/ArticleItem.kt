package com.example.almacen.catalog.presentation.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.almacen.catalog.domain.model.Article

@Composable
fun ArticleItem(article: Article) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface, // ← blanco/gris claro
            contentColor = MaterialTheme.colorScheme.onSurface   // ← texto negro/gris oscuro
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = article.nombre, style = MaterialTheme.typography.bodyLarge)
            Text(text = "ID: ${article.id}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}