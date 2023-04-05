package com.example.githubuserapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.githubuserapp.data.local.entity.UserEntity
import com.example.githubuserapp.data.local.room.UserDao
import com.example.githubuserapp.data.remote.api.ApiService
import com.example.githubuserapp.model.DetailUserResponse
import com.example.githubuserapp.model.GithubResponse
import com.example.githubuserapp.model.ItemsItem
import com.example.githubuserapp.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService : ApiService,
    private val userDao : UserDao,
    private val appExecutors : AppExecutors
){
    private val result = MediatorLiveData<Result<List<UserEntity>>>()
    private val detailUser = MediatorLiveData<Result<DetailUserResponse>>()
    private val following = MediatorLiveData<Result<List<UserEntity>>>()
    private val followers = MediatorLiveData<Result<List<UserEntity>>>()

    fun githubFindUser(query : String): LiveData<Result<List<UserEntity>>> {
        result.value = Result.Loading

        val client = apiService.getListUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse> ,
                response : Response<GithubResponse>
            ) {
                if (response.isSuccessful) {
                    val githubUser = response.body()?.items
                    val listUser = ArrayList<UserEntity>()

                    appExecutors.diskIO.execute{
                        githubUser?.forEach{ user->
                            val isFavorite = userDao.isUserFavorite(user.login)
                            val userEntity = UserEntity(
                                user.login,
                                user.avatarUrl,
                                isFavorite
                            )
                            listUser.add(userEntity)
                        }
                    }
                }
            }

            override fun onFailure(call : Call<GithubResponse> , t : Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    fun gitHubDetailUser(username : String): LiveData<Result<DetailUserResponse>>{
        result.value = Result.Loading
        val client = apiService.getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse>{
            override fun onResponse(
                call : Call<DetailUserResponse> ,
                response : Response<DetailUserResponse>
            ) {
                if (response.isSuccessful){
                    val detailDataUser = response.body() as DetailUserResponse
                    val userName = response.body() !!.name
                    detailUser.value = Result.Success(detailDataUser)
                    gitHubGetFollowers(username)
                    giHubGetFollowing(username)

                }
            }

            override fun onFailure(call : Call<DetailUserResponse> , t : Throwable) {

            }
        })
        return detailUser
    }


    fun getFollowers() = followers
    private fun gitHubGetFollowers(username : String){
        result.value = Result.Loading
        val client = apiService.getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call : Call<List<ItemsItem>> ,
                response : Response<List<ItemsItem>>
            ) {
                if(response.isSuccessful){
                    val responseFollowers = response.body()
                    val listFollowers = ArrayList<UserEntity>()
                    appExecutors.diskIO.execute {
                        responseFollowers?.forEach{ user->
                            val isFavorite = userDao.isUserFavorite(user.login)
                            val userEntity = UserEntity(
                                user.login,
                                user.avatarUrl,
                                isFavorite
                            )
                            listFollowers.add(userEntity)
                        }
                        followers.postValue(Result.Success(listFollowers))
                    }
                }
            }

            override fun onFailure(call : Call<List<ItemsItem>> , t : Throwable) {
                followers.value = Result.Error(t.message.toString())
            }

        })
    }

    fun getFollowing() = following
    private fun giHubGetFollowing(username : String){
        result.value = Result.Loading
        val client = apiService.getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call : Call<List<ItemsItem>> ,
                response : Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful){
                    val responseFollowing = response.body()
                    val listFollowing = ArrayList<UserEntity>()
                    appExecutors.diskIO.execute {
                        responseFollowing?.forEach { user->
                            val isFavorite = userDao.isUserFavorite(user.login)
                            val userEntity = UserEntity(
                                user.login,
                                user.avatarUrl,
                                isFavorite
                            )
                            listFollowing.add(userEntity)
                        }
                        following.postValue(Result.Success(listFollowing))
                    }
                }
            }

            override fun onFailure(call : Call<List<ItemsItem>> , t : Throwable) {
                following.value = Result.Error(t.message.toString())
            }

        })
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>> = userDao.getFavoriteUsers()

    suspend fun setFavoriteUser(user: UserEntity, isFavorite: Boolean) {
        user.isFavorite = isFavorite
        if (isFavorite) {
            userDao.insert(user)
        } else {
            userDao.delete(user)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao : UserDao,
            appExecutors : AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, appExecutors)
            }.also { instance = it }
    }
}