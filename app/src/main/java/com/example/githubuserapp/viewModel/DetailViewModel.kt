package com.example.githubuserapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuserapp.data.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val userRepository : UserRepository) : ViewModel() {
    fun githubDetailUser(username: String) = userRepository.gitHubDetailUser(username)

    fun getFollowers(username : String) = userRepository.gitHubGetFollowers(username)

    fun getFollowing(username : String) = userRepository.giHubGetFollowing(username)

    fun addFavorite(username : String) {
        viewModelScope.launch {
            userRepository.setFavoriteUser(username,true)
        }
    }

    fun removeFavorite(username : String) {
        viewModelScope.launch {
            userRepository.setFavoriteUser(username, false)
        }
    }
}