package com.example.githubuserapp.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.data.model.DetailUserResponse
import com.example.githubuserapp.databinding.UserItemBinding

class MainAdapterUser : RecyclerView.Adapter<MainAdapterUser.ViewHolder>(){

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DetailUserResponse> =
            object : DiffUtil.ItemCallback<DetailUserResponse>() {
                override fun areItemsTheSame(oldUser: DetailUserResponse, newUser: DetailUserResponse): Boolean {
                    return oldUser.login == newUser.login
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: DetailUserResponse, newUser: DetailUserResponse): Boolean {
                    return oldUser == newUser
                }
            }
    }

    val diffBuild = AsyncListDiffer(this, DIFF_CALLBACK)

    inner class ViewHolder(private val binding : UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user : DetailUserResponse) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .into(circleImageView)
                tvUsername.text = user.login
                tvId.text = user.type
                itemView.setOnClickListener {
                    onItemClickListener?.let { it(user) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() : Int = diffBuild.currentList.size

    override fun onBindViewHolder(holder : ViewHolder , position : Int) {
        holder.bind(diffBuild.currentList[position])
    }

    private var onItemClickListener: ((DetailUserResponse) -> Unit)? = null

    fun setOnItemClickListener(listener: (DetailUserResponse) -> Unit) {
        onItemClickListener = listener
    }

}



