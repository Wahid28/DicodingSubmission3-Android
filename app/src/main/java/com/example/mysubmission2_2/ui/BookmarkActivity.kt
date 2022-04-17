package com.example.mysubmission2_2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission2_2.databinding.ActivityBookmarkBinding
import com.example.mysubmission2_2.ui.adapter.BookmarkUserAdapter
import com.example.mysubmission2_2.ui.factory.UserBookmarkViewModelFactory
import com.example.mysubmission2_2.ui.viewmodel.UserBookmarkViewModel

class BookmarkActivity : AppCompatActivity() {

    private lateinit var userBookmarkViewModel: UserBookmarkViewModel

    private var _activityBookmarkActivityBinding: ActivityBookmarkBinding? = null
    private val binding get() = _activityBookmarkActivityBinding

    private lateinit var adapter: BookmarkUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityBookmarkActivityBinding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Favorite Users"

        userBookmarkViewModel = obtainViewModel(this)
        userBookmarkViewModel.getAllUser().observe(this, { userList ->
            if (userList != null){
                adapter.setListUser(userList)
            }
        })

        adapter = BookmarkUserAdapter()

        binding?.rvUserBookmark?.layoutManager = LinearLayoutManager(this)
        binding?.rvUserBookmark?.setHasFixedSize(true)
        binding?.rvUserBookmark?.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityBookmarkActivityBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserBookmarkViewModel {
        val factory = UserBookmarkViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UserBookmarkViewModel::class.java]
    }
}