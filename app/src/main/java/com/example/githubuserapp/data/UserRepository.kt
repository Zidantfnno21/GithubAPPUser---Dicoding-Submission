package com.example.githubuserapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.githubuserapp.data.local.room.UserDao
import com.example.githubuserapp.data.remote.api.ApiService
import com.example.githubuserapp.data.model.DetailUserResponse
import com.example.githubuserapp.helper.SettingPreferences
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val apiService : ApiService,
    private val userDao : UserDao,
    private val preferences : SettingPreferences
){

    fun githubFindUsers(username : String): LiveData<Result<List<DetailUserResponse>>> = liveData{
        emit(Result.Loading)
        try{
            val response = apiService.getListUsers(username)
            val listUser = response.items
            emit(Result.Success(listUser))
            userDao.insertUsers(listUser)
        }catch (e : Exception){
            Log.d("UserRepository" , e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun gitHubDetailUser(username : String): LiveData<Result<DetailUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val user = apiService.getDetailUser(username)
            val isFavorite = userDao.isUserFavorite(user.login)
            val detailUser = DetailUserResponse(
                login = user.login,
                htmlUrl = user.htmlUrl,
                avatarUrl =  user.avatarUrl,
                name = user.name,
                publicRepos = user.publicRepos,
                followers = user.followers,
                following = user.following,
                location = user.location,
                company = user.company,
                type = user.type,
                favorite = isFavorite
            )
            userDao.insertUser(detailUser)
        }catch (e : Exception){
            Log.d("User Repository", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<DetailUserResponse>> = userDao.getUser(username).map { Result.Success(it) }
        emitSource(localData)
    }

    fun gitHubGetFollowers(username : String): LiveData<Result<List<DetailUserResponse>>> = liveData{
        emit(Result.Loading)
        try {
            val userFollowers = apiService.getFollowers(username)
            emit(Result.Success(userFollowers))
        }catch (e :Exception){
            Log.d("User Repository", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

   fun giHubGetFollowing(username : String) : LiveData<Result<List<DetailUserResponse>>> = liveData{
        emit(Result.Loading)
        try {
            val userFollowing = apiService.getFollowing(username)
            emit(Result.Success(userFollowing))
        }catch (e :Exception){
            Log.d("User Repository", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFavoriteUser(): LiveData<List<DetailUserResponse>> = userDao.getFavoriteUsers()

    suspend fun setFavoriteUser(username : String, isFavorite: Boolean) {
        userDao.setUserFavorite(username, isFavorite)
    }
    fun getThemeSettings(): Flow<Boolean> = preferences.getThemeSetting()

    suspend fun setThemeSettings(isDarkModeActive: Boolean){
        preferences.saveThemeSetting(isDarkModeActive)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao : UserDao,
            preferences : SettingPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, preferences)
            }.also { instance = it }
    }
}