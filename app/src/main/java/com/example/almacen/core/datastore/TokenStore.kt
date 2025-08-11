package com.example.almacen.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth")

object TokenStore {
    private val KEY_TOKEN = stringPreferencesKey("token")
    private val KEY_USERNAME = stringPreferencesKey("username")
    private val KEY_IDUSER = stringPreferencesKey("iduser")

    fun tokenFlow(context: Context) = context.dataStore.data.map { it[KEY_TOKEN] }
    fun usernameFlow(context: Context) = context.dataStore.data.map { it[KEY_USERNAME] }
    fun iduserFlow(context: Context) = context.dataStore.data.map {  it[KEY_IDUSER] }

    suspend fun save(context: Context, token: String, username: String, idUser: String) {
        context.dataStore.edit {
            it[KEY_TOKEN] = token
            it[KEY_USERNAME] = username
            it[KEY_IDUSER] = idUser
        }
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit {
            it.remove(KEY_TOKEN)
            it.remove(KEY_USERNAME)
            it.remove(KEY_IDUSER)
        }
    }
}
