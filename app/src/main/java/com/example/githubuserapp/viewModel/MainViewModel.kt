package com.example.githubuserapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.Event
import com.example.githubuserapp.api.ApiConfig
import com.example.githubuserapp.model.DetailUserResponse
import com.example.githubuserapp.model.GithubResponse
import com.example.githubuserapp.model.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUserData = MutableLiveData<List<ItemsItem>>()
    val listUserData: LiveData<List<ItemsItem>> = _listUserData

    private val _detailDataUsers = MutableLiveData<DetailUserResponse>()
    val detailDataUsers : LiveData<DetailUserResponse> = _detailDataUsers

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing : LiveData<List<ItemsItem>> = _listFollowing

    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers : LiveData<List<ItemsItem>> = _listFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isNotFound = MutableLiveData<Event<String>>()
    val isNotFound : LiveData<Event<String>> = _isNotFound

    private val _isOpening = MutableLiveData<Event<Long>>(Event(500))
    val isOpening : LiveData<Event<Long>> = _isOpening

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun githubFindUser(query : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse> ,
                response : Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUserData.value = response.body()?.items

                    if (_listUserData.value !!.isEmpty()){
                        _isNotFound.value = Event("User Tidak Ditemukan")
                    }

                }else{
                    Log.e(TAG , "onFailure: ${response.message()}")

                }
            }

            override fun onFailure(call : Call<GithubResponse> , t : Throwable) {
                _isLoading.value = false
                Log.e(TAG , "onFailure: ${t.message}")
            }
        })
    }

    fun gitHubDetailUser(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse>{

            override fun onResponse(
                call : Call<DetailUserResponse> ,
                response : Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _detailDataUsers.value = response.body()

                }else{
                    Log.e(TAG , "onFailure: ${response.message()}")

                }
            }

            override fun onFailure(call : Call<DetailUserResponse> , t : Throwable) {
                _isLoading.value = false
                Log.e(TAG , "onFailure: ${t.message}")
            }
        })
    }

    fun gitHubGetFollowers(username : String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call : Call<List<ItemsItem>> ,
                response : Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _listFollowers.value = response.body()

                    if(_listFollowers.value!!.isEmpty()){
                        _isNotFound.value = Event("Followers Kosong")
                    }

                }else{
                    Log.e(TAG , "onFailure: ${response.message()}")

                }
            }

            override fun onFailure(call : Call<List<ItemsItem>> , t : Throwable) {
                _isLoading.value = false
                Log.e(TAG , "onFailure: ${t.message}")
            }

        })
    }

    fun giHubGetFollowing(username : String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call : Call<List<ItemsItem>> ,
                response : Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _listFollowing.value = response.body()

                    if (_listFollowing.value!!.isEmpty()){
                        _isNotFound.value = Event("Following Kosong")
                    }

                }else{
                    Log.e(TAG , "onFailure: ${response.message()}")

                }
            }

            override fun onFailure(call : Call<List<ItemsItem>> , t : Throwable) {
                _isLoading.value = false
                Log.e(TAG , "onFailure: ${t.message}")
            }

        })
    }
}