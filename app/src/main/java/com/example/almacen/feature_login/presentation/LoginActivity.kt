package com.example.almacen.feature_login.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.almacen.MainActivity
import com.example.almacen.feature_login.presentation.ui.LoginScreen
import com.example.almacen.core.ui.theme.AlmacenTheme
import com.example.almacen.feature_login.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlmacenTheme {
                val vm: LoginViewModel = hiltViewModel()
                val s = vm.state.value
                if (s.success){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                LoginScreen(
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
