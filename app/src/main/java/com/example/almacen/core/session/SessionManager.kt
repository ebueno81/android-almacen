package com.example.almacen.core.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore by preferencesDataStore("session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val TOKEN = stringPreferencesKey("token")
    private val USERNAME = stringPreferencesKey("username")

    val tokenFlow: Flow<String?> = context.sessionDataStore.data.map { it[TOKEN] }
    val usernameFlow: Flow<String?> = context.sessionDataStore.data.map { it [USERNAME] }

    suspend fun save(token: String, username: String) {
        context.sessionDataStore.edit {
            it[TOKEN] = token
            it[USERNAME] = username
        }
    }

    suspend fun clear() {
        context.sessionDataStore.edit { it.clear() }
    }
}