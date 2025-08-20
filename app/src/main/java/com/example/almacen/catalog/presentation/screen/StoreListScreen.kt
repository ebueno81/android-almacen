package com.example.almacen.catalog.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.almacen.catalog.presentation.screen.components.StoreItem
import com.example.almacen.catalog.presentation.viewmodel.StoreListViewModel
import com.example.almacen.core.ui.components.AppScaffold

@Composable
fun StoreListScreen(
    onBack: () -> Unit,
    vm: StoreListViewModel = hiltViewModel()
) {
    AppScaffold(
        title = "Almacenes",
        onBack = onBack
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {
            OutlinedTextField(
                value = vm.query,
                onValueChange = vm::onQueryChange,
                singleLine = true,
                label = { Text("Buscar…") },
                trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),           // ← separación entre cards
                contentPadding = PaddingValues(vertical = 8.dp)            // ← padding superior/inferior
            ) {
                items(
                    items = vm.filtered,
                    key = { it.id }                                        // estable si el id es único
                ) { s ->
                    StoreItem(
                        store = s
                    )
                    Divider()
                }
            }
        }
    }
}
