package com.example.githubuserapp.view

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.data.model.DetailUserResponse
import com.example.githubuserapp.databinding.ActivityDetailBinding
import com.example.githubuserapp.view.adapter.SectionsPagerAdapter
import com.example.githubuserapp.viewModel.DetailViewModel
import com.example.githubuserapp.viewModel.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {

    private lateinit var activityDetailBinding : ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>{
        ViewModelFactory.getInstance(application)
    }

    companion object {
        const val EXTRA_USERNAME = "username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.following ,
            R.string.follower
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        supportActionBar?.title = username
        supportActionBar?.elevation = 0f

        if (username != null) {
            setPagerAdapter(username)
            detailViewModel.githubDetailUser(username).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is com.example.githubuserapp.data.Result.Loading -> {
                            isLoading(true)
                        }
                        is com.example.githubuserapp.data.Result.Success -> {
                            isLoading(false)
                            if(result.data.favorite){
                                //activityDetailBinding.btAddFavorite.background = ContextCompat.getDrawable(applicationContext, R.drawable.button_filled)
                                activityDetailBinding.btAddFavorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.md_theme_dark_onTertiary))
                                activityDetailBinding.btAddFavorite.text = "Remove Favorito"
                            }else{
                                //activityDetailBinding.btAddFavorite.background = ContextCompat.getDrawable(applicationContext, R.drawable.button_border)
                                activityDetailBinding.btAddFavorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.md_theme_dark_primaryContainer))
                                activityDetailBinding.btAddFavorite.text = "Add to Favorito"
                            }
                            setUserData(result.data)
                        }
                        is com.example.githubuserapp.data.Result.Error -> {
                            isLoading(false)
                            Log.e(TAG , result.error)
                            Toast.makeText(
                                this@DetailActivity , "Cannot Retreive" , Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        activityDetailBinding.floatingActionButton.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/$username")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }


    private fun setPagerAdapter(username : String){
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = activityDetailBinding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = activityDetailBinding.tabs
        sectionsPagerAdapter.username = username
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    @SuppressLint("SetTextI18n")
    private fun setUserData(userResponse : DetailUserResponse) {
       activityDetailBinding.apply {
           if (userResponse.name.isNullOrBlank()){
               tvDetailId.text = userResponse.login
           }else{
               tvDetailId.text = userResponse.name
           }
           tvDetailUsername.text = userResponse.login
           Glide.with(this@DetailActivity)
               .load(userResponse.avatarUrl)
               .into(ivDetail)
           tvDetailFollowing.text = getString(R.string.number_following, userResponse.following)
           tvDetailFollower.text = getString(R.string.number_follower, userResponse.followers)

           btAddFavorite.setOnClickListener {
                if (userResponse.favorite){
                    detailViewModel.removeFavorite(userResponse.login)
                    Toast.makeText(
                        this@DetailActivity ,
                        "User ${userResponse.login} Removed From Favorito" ,
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    detailViewModel.addFavorite(userResponse.login)
                    Toast.makeText(
                        this@DetailActivity ,
                        "User ${userResponse.login} Added to Favorito" ,
                        Toast.LENGTH_SHORT
                    ).show()
                }
           }
       }
    }

    private fun isLoading(loading: Boolean) {
        activityDetailBinding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

}