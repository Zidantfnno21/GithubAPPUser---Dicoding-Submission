package com.example.githubuserapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.model.ItemsItem

class MainAdapterUser(private val userData : List<ItemsItem>) : RecyclerView.Adapter<MainAdapterUser.ViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClick(data : ItemsItem)
    }

    fun setOnItemClickCallBack(onItemClickCallback : OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val tvUser: TextView = itemView.findViewById(R.id.tvUsername)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
        val ivUser: ImageView = itemView.findViewById(R.id.circleImageView)

    }

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.user_item , parent , false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder : ViewHolder , position : Int) {
        val user = userData[position]
        holder.tvUser.text = user.login
        holder.tvId.text = user.id
        Glide.with(holder.itemView.context)
            .load(user.avatarUrl)
            .into(holder.ivUser)
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClick(userData[holder.adapterPosition])

        }
    }

    override fun getItemCount() : Int = userData.size
}



