package com.example.almacen.feature_login.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
fun LoginLogo(
    modifier: Modifier = Modifier,
    sizeDp: Int = 144
) {
    val cs = MaterialTheme.colorScheme
    Card(
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(3.dp, cs.primary),
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        modifier = modifier.size(sizeDp.dp)
    ) {
        // Ícono centrado en el círculo
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberVectorPainter(Icons.Default.Person),
                contentDescription = "User icon",
                modifier = Modifier.size((sizeDp * 0.5f).dp), // ocupa la mitad del círculo
                colorFilter = ColorFilter.tint(cs.primary)
            )
        }
    }
}

