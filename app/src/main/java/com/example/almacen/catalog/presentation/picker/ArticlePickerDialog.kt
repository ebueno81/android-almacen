package com.example.almacen.catalog.presentation.picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.almacen.catalog.domain.model.Article
import kotlinx.coroutines.flow.Flow

@Composable
fun ArticlePickerDialog(
    onDismiss: () -> Unit,
    pagingFlow: Flow<PagingData<Article>>,
    onQueryChange: (String) -> Unit,
    onSelected: (Article) -> Unit
) {
    val articles = pagingFlow.collectAsLazyPagingItems()
    var query by remember { mutableStateOf("") } // ✅ guardar el texto

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Artículo") },
        text = {
            Column {
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        onQueryChange(it) // ✅ avisa al VM
                    },
                    label = { Text("Buscar artículo...") },
                    trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn {
                    items(articles.itemCount) { idx ->
                        val art = articles[idx]
                        if (art != null) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelected(art)
                                        onDismiss()
                                    }
                                    .padding(8.dp)
                            ) {
                                Text(art.nombre)
                                Text("ID: ${art.id}")
                            }
                            Divider()
                        }
                    }
                }
            }
        },
        confirmButton = {}
    )
}