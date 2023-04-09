package com.example.githubuserapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.githubuserapp.data.UserRepository

class FavoriteViewModel(private val userRepository : UserRepository) : ViewModel() {
    fun getUserFavorite() = userRepository.getFavoriteUser()
}