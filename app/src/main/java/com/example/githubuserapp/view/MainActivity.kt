package com.example.githubuserapp.view

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.data.Result
import com.example.githubuserapp.view.adapter.MainAdapterUser
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.viewModel.MainViewModel
import com.example.githubuserapp.viewModel.helper.ViewModelFactory
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding : ActivityMainBinding
    private lateinit var adapterUser : MainAdapterUser
    private val mainViewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(application)
    }
    private var isDark: Boolean = false

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setUserData()
        supportActionBar?.show()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.group)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.elevation = 0f
        findUser()


        adapterUser.setOnItemClickListener { username ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, username.login)
            startActivity(intent)
        }


        mainViewModel.getTheme().observe(this) {isNight->
            isDark = if(isNight){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                false
            }
        }

    }

    override fun onCreateOptionsMenu(menu : Menu) : Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu , menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search)?.actionView as SearchView

        if (isDark){
            menu.getItem(2).icon = ContextCompat.getDrawable(this , R.drawable.ic_lightmode)
        } else {
            menu.getItem(2).icon = ContextCompat.getDrawable(this , R.drawable.ic_darkmode)
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query : String) : Boolean {
                if (query.isNotEmpty()){
                    findUser(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText : String?) : Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        when(item.itemId) {
            R.id.menu_favorite -> startActivity(
                Intent(
                    this@MainActivity,
                    FavoriteActivity::class.java
                )
            )
            R.id.menu_theme -> {
                lifecycleScope.launch{
                    mainViewModel.setTheme(!isDark)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun setUserData() {
        adapterUser = MainAdapterUser()
        activityMainBinding.rvMain.apply {
            adapter = adapterUser
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun findUser(query: String = "dicoding") {
        mainViewModel.gitHubFindUsers(query).observe(this@MainActivity) {result->
            if (result != null ) {
                when (result) {
                    is Result.Loading -> {
                        isLoading(true)
                    }
                    is Result.Success -> {
                        isLoading(false)
                        val userData = result.data
                        adapterUser.diffBuild.submitList(userData)
                    }
                    is Result.Error -> {
                        activityMainBinding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@MainActivity,
                            "Terjadi kesalahan" ,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun isLoading(loading: Boolean) {
        activityMainBinding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        // Cancel the coroutine to stop observing changes in the data store
        lifecycleScope.cancel()
        super.onDestroy()
    }

}