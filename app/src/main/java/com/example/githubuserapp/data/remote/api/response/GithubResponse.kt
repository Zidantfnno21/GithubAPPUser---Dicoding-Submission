package com.example.githubuserapp.data.remote.api.response

import com.example.githubuserapp.data.model.DetailUserResponse
import com.google.gson.annotations.SerializedName

data class GithubResponse(
	@field:SerializedName("total_count")
	val totalCount: Int ,

	@field:SerializedName("items")
	val items: List<DetailUserResponse> ,
)


