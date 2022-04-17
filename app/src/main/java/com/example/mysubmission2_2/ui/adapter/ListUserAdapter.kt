package com.example.mysubmission2_2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysubmission2_2.remote.response.ResponseUserDetail
import com.example.mysubmission2_2.databinding.ItemRowUserBinding

class ListUserAdapter(private val listuser: ArrayList<ResponseUserDetail>, onItemClick: OnItemClickCallback): RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback = onItemClick

    class ViewHolder (private val binding: ItemRowUserBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(user: ResponseUserDetail) {
            with(binding) {
                tvUsername.text = user.login
                tvLocation.text = user.location
                tvItemName.text = user.name
                tvCompany.text = user.company
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .into(imgItemPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.listuser[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listuser[holder.adapterPosition]) }
    }


    override fun getItemCount(): Int {
        return listuser.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ResponseUserDetail)
    }
}