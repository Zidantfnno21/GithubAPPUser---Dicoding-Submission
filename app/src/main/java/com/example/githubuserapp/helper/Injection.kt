package com.example.githubuserapp.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.githubuserapp.data.UserRepository
import com.example.githubuserapp.data.local.room.UserRoomDatabase
import com.example.githubuserapp.data.remote.api.ApiConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("DarkMode")
object Injection {
    fun provideRepository(context : Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserRoomDatabase.getInstance(context)
        val dao = database.userDao()
        val settingPreferences = SettingPreferences.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService , dao, settingPreferences)
    }
}