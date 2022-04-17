package com.example.mysubmission2_2.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mysubmission2_2.ui.viewmodel.UserBookmarkViewModel

class UserBookmarkViewModelFactory private constructor(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: UserBookmarkViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): UserBookmarkViewModelFactory {
            if (INSTANCE == null) {
                synchronized(UserBookmarkViewModelFactory::class.java) {
                    INSTANCE = UserBookmarkViewModelFactory(application)
                }
            }
            return INSTANCE as UserBookmarkViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserBookmarkViewModel::class.java)) {
            return UserBookmarkViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(UserBookmarkViewModel::class.java)) {
            return UserBookmarkViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}