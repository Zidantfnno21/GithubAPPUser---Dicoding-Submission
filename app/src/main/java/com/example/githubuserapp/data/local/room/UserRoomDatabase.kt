package com.example.githubuserapp.data.local.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserRoomDatabase? = null
        fun getInstance(context: Context): UserRoomDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDatabase::class.java,
                    "UsersFavorite"
                ).build()
            }
    }
}