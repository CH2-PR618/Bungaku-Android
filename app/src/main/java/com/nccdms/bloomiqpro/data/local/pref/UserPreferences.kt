package com.nccdms.bloomiqpro.data.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences
@Inject constructor(
    private val dataStore: DataStore<Preferences>) {

    fun getToken(): Flow<String>{
        return dataStore.data.map {pref ->
            pref[TOKEN_KEY] ?: ""
        }
    }

    suspend fun setToken(token:String){
        dataStore.edit {pref ->
            pref[TOKEN_KEY] = token
        }
    }

    suspend fun clearSession(){
        dataStore.edit {pref ->
            pref.clear()
        }
    }

    companion object{
        private val TOKEN_KEY = stringPreferencesKey("token_key")
    }
}

