package com.example.githubuserapp.data.datastore

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataStore(private val context: Context) {

    private val Context.userPreferencesDataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
        name = "Settings"
    )

    fun getThemeSetting(): Flow<Boolean> {
        return context.userPreferencesDataStore.data.map { preferences ->
            preferences[booleanPreferencesKey("theme_setting")] ?: false
        }
    }

    suspend fun setThemeSetting(isDarkMode: Boolean) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[booleanPreferencesKey("theme_setting")] = isDarkMode
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: UserDataStore? = null

        fun getInstance(context: Context): UserDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = UserDataStore(context)
                INSTANCE = instance
                instance
            }
        }
    }
}