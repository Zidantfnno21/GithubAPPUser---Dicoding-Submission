package com.example.githubuserapp.view

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.view.adapter.MainAdapterUser
import com.example.githubuserapp.model.ItemsItem
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding : ActivityMainBinding
    private lateinit var mainViewModel : MainViewModel
    private val handler = Handler()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        supportActionBar?.show()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.group)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.elevation = 0f
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        mainViewModel.listUserData.observe(
            this ,
        ) { listUserData ->
            setUserData(listUserData)
        }

        mainViewModel.isLoading.observe(this,
        ){
            isLoading(it)
        }

        mainViewModel.isNotFound.observe(this,
        ){
            it.getContentIfNotHandled()?.let { text->
                Snackbar.make(
                    window.decorView.rootView,
                    text,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        mainViewModel.isOpening.observe(this){
            it.getContentIfNotHandled()?.let { aLong->
                handler.postDelayed({ val rootView = findViewById<View>(android.R.id.content)
                    Snackbar.make(rootView, "Mulailah Mencari User", Snackbar.LENGTH_LONG).setDuration(1000).show()
                                    }, aLong)
            }
        }

    }

    override fun onCreateOptionsMenu(menu : Menu) : Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu , menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query : String) : Boolean {

                if (query.isNotEmpty()){
                    mainViewModel.run { githubFindUser(query) }
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
            R.id.menu_theme -> TODO()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserData(listUserData : List<ItemsItem>) {
        val adapter = MainAdapterUser(listUserData)

        adapter.setOnItemClickCallBack(object : MainAdapterUser.OnItemClickCallback {
            override fun onItemClick(data : ItemsItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("username", data.login)
                startActivity(intent)
            }
        })

        activityMainBinding.rvMain.adapter = adapter
        activityMainBinding.rvMain.layoutManager = LinearLayoutManager(this)

    }

    private fun isLoading(loading: Boolean) {
        activityMainBinding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

}