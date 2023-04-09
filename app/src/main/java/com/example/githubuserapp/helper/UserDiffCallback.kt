package com.example.githubuserapp.viewModel.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.githubuserapp.data.model.DetailUserResponse

class UserDiffCallback : DiffUtil.ItemCallback<DetailUserResponse>() {
    override fun areItemsTheSame(oldItem : DetailUserResponse , newItem : DetailUserResponse) : Boolean {
        return oldItem.login == newItem.login
    }

    override fun areContentsTheSame(oldItem : DetailUserResponse , newItem : DetailUserResponse) : Boolean {
        return  oldItem == newItem
    }
}