package com.example.almacen.feature_login.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.almacen.MainActivity
import com.example.almacen.core.datastore.RememberStore
import com.example.almacen.core.security.PasswordVault
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_login.presentation.ui.LoginScreen
import com.example.almacen.feature_login.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlmacenTheme {
                val vm: LoginViewModel = hiltViewModel()
                val s = vm.state.value
                val ctx = LocalContext.current

                val remembered by RememberStore.rememberFlow(ctx)
                    .collectAsState(initial = false)
                val rememberedIdUser by RememberStore.idUserFlow(ctx)
                    .collectAsState(initial="")

                val rememberedPass = remember { mutableStateOf<String?>(null) }
                LaunchedEffect(Unit) {rememberedPass.value = PasswordVault.get(ctx) }

                if (s.success){
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    finish()
                }

                LoginScreen(
                    initialUser = rememberedIdUser ?: "",
                    initialPass = rememberedPass.value ?: "",
                    initialRemember = remembered,
                    onRememberChange = vm::setRemember,
                    onLogin = { user, pass, _ ->
                        vm.login(this, user, pass)
                    },
                    onClose = { finish() },
                    onRecoverPassword = {
                        // TODO: flujo de recuperación
                    },
                    isLoading = s.isLoading,
                    errorMessage = s.error
                )
            }
        }
    }
}
