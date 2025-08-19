package com.example.almacen.feature_activity.presentation.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDialog(
    show: Boolean,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String = "Confirmación",
    confirmText: String = "Sí",
    dismissText: String = "No",
    dismissOnOutside: Boolean = true
) {
    if (!show) return

    AlertDialog(
        onDismissRequest = {
            if (dismissOnOutside) onDismiss()
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(confirmText) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(dismissText) }
        },
        title = { Text(title) },
        text = { Text(message) }
    )
}
