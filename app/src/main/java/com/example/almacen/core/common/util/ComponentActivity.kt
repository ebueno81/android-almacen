package com.example.almacen.util

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import com.example.almacen.MainActivity
import kotlin.reflect.KClass

fun ComponentActivity.goHome() {
    startActivity(
        Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    )
    finish()
}

fun <T: Activity> ComponentActivity.goTab(target: KClass<T>) {
    startActivity(
        Intent(this, target.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    )
    // Si no quieres que quede en el backstack:
    finish()
}
