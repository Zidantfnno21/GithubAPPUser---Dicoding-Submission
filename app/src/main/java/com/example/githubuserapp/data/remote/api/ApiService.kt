package com.example.githubuserapp.data.remote.api

import com.example.githubuserapp.data.model.DetailUserResponse
import com.example.githubuserapp.data.remote.api.response.GithubResponse
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    suspend fun getListUsers(
        @Query("q")
        page:String
    ): GithubResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username")
        username : String
    ): DetailUserResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username:String
    ): List<DetailUserResponse>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username : String
    ): List<DetailUserResponse>
}