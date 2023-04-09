package com.example.githubuserapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapp.R
import com.example.githubuserapp.data.model.DetailUserResponse
import com.example.githubuserapp.view.adapter.MainAdapterUser
import com.example.githubuserapp.viewModel.DetailViewModel
import com.example.githubuserapp.viewModel.helper.ViewModelFactory

class DetailFragment : Fragment() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var progressBar : ProgressBar
    private val detailViewModel by viewModels<DetailViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }

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

        val username = arguments?.getString(DetailActivity.EXTRA_USERNAME)
        val position = arguments?.getInt(ARG_SECTION_NUMBER)

        if (position == 1){
            if (username != null) {
                detailViewModel.getFollowing(username).observe(viewLifecycleOwner) {result->
                    if(result != null) {
                        when(result) {
                            is com.example.githubuserapp.data.Result.Loading -> {
                                isLoading(true)
                            }
                            is com.example.githubuserapp.data.Result.Success -> {
                                isLoading(false)
                                setUserFollowing(result.data)
                            }
                            is com.example.githubuserapp.data.Result.Error -> {

                            }
                        }
                    }
                }
            }
        }else{
            if (username != null) {
                detailViewModel.getFollowers(username).observe(viewLifecycleOwner) {result->
                    if(result != null) {
                        when(result) {
                            is com.example.githubuserapp.data.Result.Loading -> {
                                isLoading(true)
                            }
                            is com.example.githubuserapp.data.Result.Success -> {
                                isLoading(false)
                                setUserFollowers(result.data)
                            }
                            is com.example.githubuserapp.data.Result.Error -> {

                            }
                        }
                    }
                }
            }
        }
    }

    private fun setUserFollowing(listFollowing : List<DetailUserResponse>) {
        val adapter = MainAdapterUser()
        adapter.diffBuild.submitList(listFollowing)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun setUserFollowers(listFollowers : List<DetailUserResponse>) {
        val adapter = MainAdapterUser()
        adapter.diffBuild.submitList(listFollowers)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun isLoading(loading: Boolean) {
            progressBar.visibility = if(loading) View.VISIBLE else View.GONE
    }
}