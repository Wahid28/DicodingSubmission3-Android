package com.example.mysubmission2_2.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.mysubmission2_2.data.User
import com.example.mysubmission2_2.data.UserDao
import com.example.mysubmission2_2.data.UserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllUser(): LiveData<List<User>> = mUserDao.getAllUser()

    fun insert(user: User){
        executorService.execute {mUserDao.insert(user)}
    }

    fun delete(user: User) {
        executorService.execute { mUserDao.delete(user) }
    }

    fun isBookmarked(username: String): LiveData<Boolean> = liveData {
        emitSource(mUserDao.isUserBookmarked(username))
    }

}