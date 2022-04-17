package com.example.mysubmission2_2.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.mysubmission2_2.data.User

class UserDiffCallback(private val mOldNoteList: List<User>, private val mNewNoteList: List<User>) : DiffUtil.Callback()  {
    override fun getOldListSize(): Int {
        return mOldNoteList.size
    }

    override fun getNewListSize(): Int {
        return mNewNoteList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldNoteList[oldItemPosition].username == mNewNoteList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldNoteList[oldItemPosition]
        val newEmployee = mNewNoteList[newItemPosition]
        return oldEmployee.name == newEmployee.name && oldEmployee.avatarUrl == newEmployee.avatarUrl
    }
}