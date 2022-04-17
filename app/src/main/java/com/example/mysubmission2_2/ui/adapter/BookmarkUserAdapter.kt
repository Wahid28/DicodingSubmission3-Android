package com.example.mysubmission2_2.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysubmission2_2.data.User
import com.example.mysubmission2_2.databinding.ItemRowUserBinding
import com.example.mysubmission2_2.ui.MoveDataWithActivity
import com.example.mysubmission2_2.helper.UserDiffCallback

class BookmarkUserAdapter: RecyclerView.Adapter<BookmarkUserAdapter.ViewHolder>() {

    private val listUser = ArrayList<User>()

    fun setListUser(listUser: List<User>){
        val diffCallback = UserDiffCallback(this.listUser, listUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUser.clear()
        this.listUser.addAll(listUser)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder (private val binding: ItemRowUserBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(user:User){
            with(binding){
                tvUsername.text = user.username
                tvLocation.text = user.location
                tvItemName.text = user.name
                tvCompany.text = user.company
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .into(imgItemPhoto)
                cardView.setOnClickListener{
                    val intent = Intent(it.context, MoveDataWithActivity::class.java)
                    intent.putExtra(MoveDataWithActivity.EXTRA_USER_BOOKMARK, user)
                    it.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

}