package com.example.githubuserapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapp.R
import com.example.githubuserapp.view.adapter.MainAdapterUser
import com.example.githubuserapp.model.ItemsItem
import com.example.githubuserapp.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class DetailFragment : Fragment() {

    private lateinit var mainViewModel : MainViewModel
    private lateinit var recyclerView : RecyclerView
    private lateinit var progressBar : ProgressBar

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View? {

        val view = inflater.inflate(R.layout.fragment_detail , container , false)
        recyclerView = view.findViewById(R.id.rvDetail)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        progressBar = view.findViewById(R.id.progressBarDetail)

        return view
    }

    companion object{
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_NAME = "username"
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        val position = arguments?.getInt(ARG_SECTION_NUMBER)
        val index = arguments?.getString(ARG_NAME)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        if (position == 1){
            mainViewModel.giHubGetFollowing(index.toString())
            mainViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
                setUserFollowing(listFollowing)
            }

        }else{
            mainViewModel.gitHubGetFollowers(index.toString())
            mainViewModel.listFollowers.observe(viewLifecycleOwner) { listFollowers ->
                setUserFollowers(listFollowers)
            }
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner){
            isLoading(it)
        }

        mainViewModel.isNotFound.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { text->
                Snackbar.make(
                    requireView(),
                    text,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun setUserFollowers(listFollowers : List<ItemsItem>) {
        val adapter = MainAdapterUser(listFollowers)
        recyclerView.adapter = adapter

        adapter.setOnItemClickCallBack(object : MainAdapterUser.OnItemClickCallback {
            override fun onItemClick(data : ItemsItem) {
                Toast.makeText(context , "You Just Click ^^" , Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUserFollowing(listFollowing : List<ItemsItem>) {
        val adapter = MainAdapterUser(listFollowing)
        recyclerView.adapter = adapter

        adapter.setOnItemClickCallBack(object : MainAdapterUser.OnItemClickCallback {
            override fun onItemClick(data : ItemsItem) {
                Toast.makeText(context , "You Just Click ^^" , Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun isLoading(loading: Boolean) {
            progressBar.visibility = if(loading) View.VISIBLE else View.GONE
    }
}