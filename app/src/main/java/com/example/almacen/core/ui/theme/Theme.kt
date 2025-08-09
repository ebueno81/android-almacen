package com.example.almacen.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = OnGreenPrimary,
    secondary = GreenSecondary,
    onSecondary = OnGreenSecondary,
    primaryContainer = GreenContainer,
    onPrimaryContainer = OnGreenContainer,
    background = Bg,
    surface = Surface,
    onSurface = OnSurface,
    outline = Outline,
    error = ErrorRed
)

@Composable
fun AlmacenTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,        // paleta VERDE global
        typography = Typography(),        // o tu Typography de Type.kt
        content = content
    )
}
