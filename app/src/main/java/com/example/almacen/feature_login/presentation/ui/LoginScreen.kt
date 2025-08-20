package com.example.almacen.feature_login.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,

    initialUser: String,
    initialPass: String,
    initialRemember: Boolean,
    onRememberChange: (Boolean) -> Unit,

    onLogin: (user: String, pass: String, remember: Boolean) -> Unit,
    onClose: () -> Unit,
    onRecoverPassword: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
) {
    val focusManager = LocalFocusManager.current
    val cs = MaterialTheme.colorScheme

    // ⬇️ estados inicializados con los props
    var user by rememberSaveable { mutableStateOf(initialUser) }
    var pass by rememberSaveable { mutableStateOf(initialPass) }
    var remember by rememberSaveable { mutableStateOf(initialRemember) }
    var showPass by rememberSaveable { mutableStateOf(false) }

    // ⬇️ si cambian desde afuera (p.ej. tras leer DataStore), sincroniza
    LaunchedEffect(initialUser, initialPass, initialRemember) {
        user = initialUser
        pass = initialPass
        remember = initialRemember
        onRememberChange(remember)
    }

    val canSubmit = user.isNotBlank() && pass.isNotBlank() && !isLoading

    Scaffold(containerColor = cs.background) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            // header degradado (se mantienen tus colores)
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .background(Brush.verticalGradient(listOf(cs.secondary, cs.primary)))
            )

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 72.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginLogo()
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cs.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Usuario", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = user,
                            onValueChange = { user = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = cs.primary,
                                unfocusedBorderColor = cs.outline,
                                cursorColor = cs.primary
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        Text("Contraseña", style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            value = pass,
                            onValueChange = { pass = it },
                            singleLine = true,
                            visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showPass = !showPass }) {
                                    Icon(
                                        imageVector = if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (showPass) "Ocultar" else "Mostrar"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Password
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    if (canSubmit) onLogin(user, pass, remember)
                                }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = cs.primary,
                                unfocusedBorderColor = cs.outline,
                                cursorColor = cs.primary
                            )
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Checkbox(
                                checked = remember,
                                onCheckedChange = {
                                    remember = it
                                    onRememberChange(it)  // ← avisa al ViewModel
                                },
                                colors = CheckboxDefaults.colors(checkedColor = cs.primary)
                            )
                            Text(
                                text = "Recordar contraseña",
                                modifier = Modifier.clickable {
                                    val newVal = !remember
                                    remember = newVal
                                    onRememberChange(newVal)
                                }
                            )
                        }

                        if (errorMessage != null) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = onClose,
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = cs.primary),
                                border = BorderStroke(1.dp, cs.primary)
                            ) { Text("Cerrar") }

                            Button(
                                onClick = { onLogin(user, pass, remember) },
                                modifier = Modifier.weight(1f),
                                enabled = canSubmit
                            ) { Text(if (isLoading) "Ingresando..." else "Iniciar") }
                        }

                        Spacer(Modifier.height(12.dp))

                        TextButton(onClick = onRecoverPassword, enabled = !isLoading) {
                            Text("¿Olvidaste tu contraseña?", color = cs.primary)
                        }
                    }
                }
            }
        }
    }
}

