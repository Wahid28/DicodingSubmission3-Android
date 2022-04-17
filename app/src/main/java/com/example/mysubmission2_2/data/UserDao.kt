package com.example.mysubmission2_2.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM user ORDER BY username ASC")
    fun getAllUser():LiveData<List<User>>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username = :username)")
    fun isUserBookmarked(username: String): LiveData<Boolean>
}