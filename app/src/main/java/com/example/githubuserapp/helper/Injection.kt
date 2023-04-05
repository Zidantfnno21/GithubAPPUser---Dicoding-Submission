package com.example.githubuserapp.viewModel.helper

import android.content.Context
import com.example.githubuserapp.data.UserRepository
import com.example.githubuserapp.data.local.room.UserRoomDatabase
import com.example.githubuserapp.data.remote.api.ApiConfig
import com.example.githubuserapp.utils.AppExecutors

object Injection {
    fun provideRepository(context : Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserRoomDatabase.getInstance(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService,dao,appExecutors)
    }
}