package com.example.almacen.catalog.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.almacen.catalog.domain.model.Reason
import com.example.almacen.catalog.presentation.viewmodel.ReasonListViewModel

@Composable
fun ReasonListScreen(
    modifier: Modifier = Modifier,
    vm: ReasonListViewModel = hiltViewModel()
) {
    val all = vm.all
    val query = vm.query
    val filtered = vm.filtered

    Column(
        modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = vm::onQueryChange,
            singleLine = true,
            label = { Text("Buscar motivo…") },
            trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        if (all.isEmpty()) {
            Text("No hay motivos disponibles", modifier = Modifier.padding(12.dp))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(filtered.size) { idx ->
                    val r: Reason = filtered[idx]
                    ListItem(
                        headlineContent = { Text(r.nombre) },
                        supportingContent = { Text("Código: ${r.id}") }
                    )
                    Divider()
                }
            }
        }
    }
}
