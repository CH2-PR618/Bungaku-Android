package com.nccdms.bloomiqpro.data.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences
@Inject constructor(
    private val dataStore: DataStore<Preferences>) {

    suspend fun getSessionId(): String {
        return dataStore.data.map { pref ->
            pref[SESSION_ID] ?: ""
        }.first()
    }

    suspend fun saveSessionId(sessionId: String) {
        dataStore.edit { preferences ->
            preferences[SESSION_ID] = sessionId
        }
    }

    suspend fun clearSessionId() {
        dataStore.edit { preferences ->
            preferences.remove(SESSION_ID)
        }
    }

    companion object {
        private val SESSION_ID = stringPreferencesKey("session_id")
    }
}

