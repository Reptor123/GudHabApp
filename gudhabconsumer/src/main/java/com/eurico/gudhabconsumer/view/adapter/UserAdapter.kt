package com.eurico.gudhabconsumer.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eurico.gudhabconsumer.Model.User
import com.eurico.gudhabconsumer.R
import com.eurico.gudhabconsumer.view.activity.DetailActivity
import com.eurico.gudhabconsumer.databinding.GridUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserHolder>() {
    var listuser = ArrayList<User>()

    fun setter(item: ArrayList<User>) {
        listuser.clear()
        listuser.addAll(item)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): UserHolder {
        val binding: GridUserBinding =
            GridUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return UserHolder(binding)
    }

    override fun getItemCount(): Int = listuser.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user = listuser[position]
        holder.bind(user)
    }

    inner class UserHolder(private val binding: GridUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.Avatar)
                .apply(RequestOptions().override(200, 200))
                .fitCenter()
                .placeholder(R.drawable.github)
                .error(R.color.white)
                .into(binding.foto)
            binding.nama.text = user.Username
            itemView.setOnClickListener {
                val context = binding.nama.context
                val movetoDetail = Intent(context, DetailActivity::class.java)
                movetoDetail.putExtra(DetailActivity.EXTRA_USER, user)
                binding.nama.context.startActivity(movetoDetail)
            }

        }
    }
}