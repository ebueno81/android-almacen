package com.example.almacen.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.moshi.JsonReader.Token
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth")

object TokenStore{
    private val KEY = stringPreferencesKey("token")

    fun tokenFlow(context: Context)=context.dataStore.data.map { it [KEY]}

    suspend fun save(context: Context, token: String){
        context.dataStore.edit { it[KEY]=token }
    }

    suspend fun clear(context: Context){
        context.dataStore.edit { it.remove(KEY) }
    }
}