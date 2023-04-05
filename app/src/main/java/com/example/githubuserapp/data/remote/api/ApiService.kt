package com.example.githubuserapp.data.remote.api

import com.example.githubuserapp.model.DetailUserResponse
import com.example.githubuserapp.model.GithubResponse
import com.example.githubuserapp.model.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    fun getListUsers(
        @Query("q")
        page:String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username")
        username : String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username")
        username:String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username")
        username : String
    ): Call<List<ItemsItem>>
}