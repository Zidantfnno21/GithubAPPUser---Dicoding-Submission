package com.example.githubuserapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuserapp.data.model.DetailUserResponse

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUsers(): LiveData<List<DetailUserResponse>>

    @Query("SELECT * FROM user WHERE login = :username")
    fun getUser(username: String): LiveData<DetailUserResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: DetailUserResponse)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<DetailUserResponse>)

    @Delete
    suspend fun delete(user: DetailUserResponse)

    @Query("SELECT * FROM user WHERE favorite = 1")
    fun getFavoriteUsers(): LiveData<List<DetailUserResponse>>

    @Query("SELECT EXISTS (SELECT * FROM user WHERE login = :username AND favorite = 1)")
    suspend fun isUserFavorite(username: String): Boolean

    @Query("UPDATE user SET favorite = :favorite WHERE login = :username")
    suspend fun setUserFavorite(username: String, favorite: Boolean)


}