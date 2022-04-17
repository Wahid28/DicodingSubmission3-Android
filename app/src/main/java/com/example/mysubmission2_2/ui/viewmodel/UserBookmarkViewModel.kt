package com.example.mysubmission2_2.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mysubmission2_2.data.User
import com.example.mysubmission2_2.data.repository.UserRepository

class UserBookmarkViewModel(application: Application): ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)

    fun getAllUser(): LiveData<List<User>> = mUserRepository.getAllUser()

    fun insert(user: User) {
        mUserRepository.insert(user)
    }

    fun delete(user: User) {
        mUserRepository.delete(user)
    }
    fun isBookmarked(username: String): LiveData<Boolean>{
        return mUserRepository.isBookmarked(username)
    }
}