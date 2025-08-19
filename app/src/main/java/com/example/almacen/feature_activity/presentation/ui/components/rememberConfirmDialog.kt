package com.example.almacen.feature_activity.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun rememberConfirmDialog(showInitially: Boolean = false) =
    rememberSaveable { mutableStateOf(showInitially) }
