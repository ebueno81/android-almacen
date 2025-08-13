package com.example.almacen.catalog.presentation.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.example.almacen.catalog.domain.model.Client
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientPickerDialog(
    onDismiss: () -> Unit,
    onSelected: (Client) -> Unit,
    pagingFlow: Flow<PagingData<Client>>,
    onQueryChange: (String?) -> Unit
) {

    var query by remember { mutableStateOf("") }
    LaunchedEffect(query) {
        onQueryChange(query.takeIf { it.isNotBlank() }) // null si está vacío
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Seleccionar cliente") },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar…") },
                    singleLine = true,
                    trailingIcon = { Icon(Icons.Default.Search, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                }
        }
    )
}

@Composable private fun CenteredProgress() { Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() } }
@Composable private fun ErrorRow(msg: String) { Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) { Text(msg) } }
