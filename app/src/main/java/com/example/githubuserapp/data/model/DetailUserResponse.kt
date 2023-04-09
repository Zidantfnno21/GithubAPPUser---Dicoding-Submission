package com.example.githubuserapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class DetailUserResponse(
	@PrimaryKey
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("html_url")
	val htmlUrl: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("name")
	var name: String? = "-",

	@field:SerializedName("public_repos")
	@ColumnInfo("public_repos")
	var publicRepos: Int? = 0,

	@field:SerializedName("followers")
	var followers: Int,

	@field:SerializedName("following")
	var following: Int,

	@field:SerializedName("location")
	var location: String? = "-",

	@field:SerializedName("company")
	var company: String? = "-",

	var favorite: Boolean
): Parcelable
