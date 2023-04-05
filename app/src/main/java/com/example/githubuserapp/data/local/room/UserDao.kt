package com.example.githubuserapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuserapp.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(users: UserEntity)

    @Delete
    suspend fun delete(users: UserEntity)

    @Query("SELECT * FROM users WHERE is_favorite = 1")
    fun getFavoriteUsers(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS (SELECT * FROM users WHERE username = :username AND is_favorite = 1)")
    fun isUserFavorite(username: String): Boolean
}