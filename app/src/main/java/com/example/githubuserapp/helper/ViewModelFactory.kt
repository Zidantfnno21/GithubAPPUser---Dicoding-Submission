package com.example.githubuserapp.viewModel.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserapp.data.UserRepository
import com.example.githubuserapp.helper.Injection
import com.example.githubuserapp.viewModel.DetailViewModel
import com.example.githubuserapp.viewModel.FavoriteViewModel
import com.example.githubuserapp.viewModel.MainViewModel


class ViewModelFactory private constructor(private val userRepository : UserRepository) :
    ViewModelProvider.NewInstanceFactory() {


        companion object {
            @Volatile
            private var INSTANCE: ViewModelFactory? = null

            @JvmStatic
            fun getInstance(context : Context): ViewModelFactory {
                if (INSTANCE == null) {
                    synchronized(ViewModelFactory::class.java) {
                        INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                    }
                }
                return INSTANCE as ViewModelFactory
            }
        }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepository) as T
        }else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(userRepository) as T
        }else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}