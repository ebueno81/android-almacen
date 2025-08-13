// com/example/almacen/core/session/RememberSession.kt
package com.example.almacen.core.session

import android.content.Context
import com.example.almacen.core.datastore.RememberStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RememberSession @Inject constructor(
    @ApplicationContext private val ctx: Context
) {
    val userCodeFlow: Flow<String?> = RememberStore.idUserFlow(ctx)
    val rememberFlow: Flow<Boolean> = RememberStore.rememberFlow(ctx)
}
