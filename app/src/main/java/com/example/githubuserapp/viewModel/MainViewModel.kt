package com.example.githubuserapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuserapp.data.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository : UserRepository) : ViewModel() {

    fun gitHubFindUsers(query : String ) = userRepository.githubFindUsers(query)

    fun getTheme(): LiveData<Boolean> = userRepository.getThemeSettings().asLiveData(viewModelScope.coroutineContext)

    fun setTheme(isDarkModeActive : Boolean) {
        viewModelScope.launch {
            userRepository.setThemeSettings(isDarkModeActive)
        }
    }
}