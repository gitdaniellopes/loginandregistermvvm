package com.ocanha.retrofitcomkotlin.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SessionDataStore constructor(
    private val context: Context
) : SessionInterface {

    companion object {
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "session_db")
        private val KEY_AUTH = stringPreferencesKey("key_auth")
    }

    override val token: Flow<String?>
        get() = context.datastore.data.map { preferences ->
            preferences[KEY_AUTH] ?: ""
        }

    override suspend fun saveToken(token: String) {
        context.datastore.edit { preferences ->
            preferences[KEY_AUTH] = token
        }
    }

    override suspend fun clear() {
        context.datastore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun isUserLogged(): Boolean {
        val isLogged = context.datastore.data.first()
        if (isLogged.contains(KEY_AUTH)) {
            return true
        }
        return false
    }
}