package com.example.almacen.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.rememberDataStore by preferencesDataStore("remember_login")

object RememberStore{

    private val KEY_REMEMBER = booleanPreferencesKey("remember")
    private val KEY_IDUSER = stringPreferencesKey("idUser")

    fun rememberFlow(ctx: Context): Flow<Boolean> =
        ctx.rememberDataStore.data.map { it[KEY_REMEMBER] ?: false }

    fun idUserFlow(ctx: Context): Flow<String?> =
        ctx.rememberDataStore.data.map { it[KEY_IDUSER] }

    suspend fun save(ctx: Context, remember: Boolean, idUser: String?){
        ctx.rememberDataStore.edit {
            it[KEY_REMEMBER] = remember
            if (remember && !idUser.isNullOrBlank())
                it[KEY_IDUSER] =idUser
            else
                it.remove(KEY_IDUSER)
        }
    }

    suspend fun clear(ctx: Context){
        ctx.rememberDataStore.edit {
            it.remove(KEY_REMEMBER)
            it.remove(KEY_IDUSER)
        }
    }
}