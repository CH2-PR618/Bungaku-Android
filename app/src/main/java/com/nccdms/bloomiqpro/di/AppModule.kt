package com.nccdms.bloomiqpro.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nccdms.bloomiqpro.data.local.pref.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("preferences")

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context):DataStore<Preferences>{
        return appContext.dataStore
    }

    @Singleton
    @Provides
    fun providePreferencesManager(dataStore: DataStore<Preferences>) = UserPreferences(dataStore)

}