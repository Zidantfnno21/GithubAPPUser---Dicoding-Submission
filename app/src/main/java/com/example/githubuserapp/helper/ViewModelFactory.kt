package com.example.githubuserapp.viewModel.helper

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserapp.data.UserRepository
import com.example.githubuserapp.viewModel.MainViewModel

class ViewModelFactory private constructor(private val userRepository : UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

        companion object {
            @Volatile
            private var INSTANCE: ViewModelFactory? = null

            @JvmStatic
            fun getInstance(context : Context): ViewModelFactory =
                INSTANCE ?: synchronized(this) {
                    INSTANCE?: ViewModelFactory(Injection.provideRepository(context))
                }
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}