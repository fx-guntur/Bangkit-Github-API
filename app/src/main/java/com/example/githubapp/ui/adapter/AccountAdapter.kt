package com.example.githubapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.data.response.ItemsItem
import com.example.githubapp.databinding.AccountRowItemBinding

class AccountAdapter(private val isClickable: Boolean? = true) :
    RecyclerView.Adapter<AccountAdapter.MyViewHolder>() {

    private var listUserData: List<ItemsItem> = listOf()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUserData(data: List<ItemsItem>) {
        listUserData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            AccountRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listUserData.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val account = listUserData[position]
        holder.bind(account)
        if (isClickable!!) {
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listUserData[holder.adapterPosition].login)
            }
        }
    }

    class MyViewHolder(val binding: AccountRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(account: ItemsItem) {
            with(binding) {
                tvUsername.text = account.login
                Glide.with(root.context)
                    .load(account.avatarUrl)
                    .into(ivProfile)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(username: String)
    }

}