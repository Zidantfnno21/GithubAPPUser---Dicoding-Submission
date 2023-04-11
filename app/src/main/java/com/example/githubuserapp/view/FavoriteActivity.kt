package com.example.githubuserapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.databinding.ActivityFavoriteBinding
import com.example.githubuserapp.view.adapter.MainAdapterUser
import com.example.githubuserapp.viewModel.FavoriteViewModel
import com.example.githubuserapp.viewModel.helper.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFavoriteBinding
    private lateinit var userAdapter : MainAdapterUser
    private val favoriteViewModel by viewModels<FavoriteViewModel>{
        ViewModelFactory.getInstance(application)
    }


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f
        setUserData()

        favoriteViewModel.getUserFavorite().observe(this@FavoriteActivity) { result->
            binding.progressBar2.visibility = View.GONE
            if (result.isEmpty()) binding.imageView.visibility = View.VISIBLE
            userAdapter.diffBuild.submitList(result)
        }

        userAdapter.setOnItemClickListener {
            val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, it.login)
            startActivity(intent)
        }
    }

    private fun setUserData() {
        userAdapter = MainAdapterUser()
        binding.rvFavorite.apply {
            adapter = userAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
        }
    }
}