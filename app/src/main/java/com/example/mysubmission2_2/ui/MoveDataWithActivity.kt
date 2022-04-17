package com.example.mysubmission2_2.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mysubmission2_2.R
import com.example.mysubmission2_2.remote.response.ResponseUserDetail
import com.example.mysubmission2_2.data.User
import com.example.mysubmission2_2.databinding.ActivityMoveWithDataBinding
import com.example.mysubmission2_2.ui.adapter.SectionsPagerAdapter
import com.example.mysubmission2_2.ui.factory.UserBookmarkViewModelFactory
import com.example.mysubmission2_2.ui.viewmodel.UserBookmarkViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MoveDataWithActivity: AppCompatActivity() {

    private var _activityMoveWithData : ActivityMoveWithDataBinding? = null
    private val binding get() = _activityMoveWithData

    private val userBookmarkViewModel : UserBookmarkViewModel by viewModels { UserBookmarkViewModelFactory.getInstance(application) }
    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMoveWithData = ActivityMoveWithDataBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = SectionsPagerAdapter(this)

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs,viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val userBookmark = intent.getParcelableExtra<User>(EXTRA_USER_BOOKMARK)
        val user = intent.getParcelableExtra<ResponseUserDetail>(EXTRA_USER)

        if (user != null){
            setData(user)
        } else if (userBookmark != null){
            setBookmarkData(userBookmark)
        }

        supportActionBar?.title = USERNAME

        Toast.makeText(this, "Hello $USERNAME", Toast.LENGTH_SHORT).show()
    }

    private fun setBookmarkData(user: User){

        USERNAME = user.username

        userBookmarkViewModel.isBookmarked(user.username).observe(this, { isBookmarked ->
            if (isBookmarked){
                binding?.bookmarkUser?.setImageResource(R.drawable.ic_bookmarked_white)
                this.isBookmarked = true
            } else{
                binding?.bookmarkUser?.setImageResource(R.drawable.ic_bookmark_border_white)
                this.isBookmarked = false
            }
        })

        binding?.apply {
            namaUser.text = user.name
            followersUser.text = user.follower.toString()
            followingUser.text = user.following.toString()
            usernameUser.text = getString(R.string.username_tag, user.username)
            locationUser.text = getString(R.string.location_tag, user.location)
            companyUser.text = getString(R.string.company_tag, user.company)
            repositoryUser.text = user.publicRepos.toString()
        }
        binding?.photoUser?.let {
            Glide.with(this)
                .load(user.avatarUrl)
                .into(it)
        }

        binding?.bookmarkUser?.setOnClickListener {
            if (isBookmarked){
                userBookmarkViewModel.delete(User(user.username,user.name,user.location, user.company, user.follower, user.following, user.avatarUrl, user.publicRepos, false))
                Toast.makeText(this, "Data $USERNAME Terhapus dari Favorit", Toast.LENGTH_SHORT).show()
            } else{
                userBookmarkViewModel.insert(User(user.username,user.name,user.location, user.company, user.follower, user.following, user.avatarUrl, user.publicRepos, true))
                Toast.makeText(this, "Data $USERNAME Tersimpan sebagai Favorit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setData(user: ResponseUserDetail){

        Log.d("Detail Data", user.name.toString())

        USERNAME = user.login

        userBookmarkViewModel.isBookmarked(user.login).observe(this, { isBookmarked ->
            if (isBookmarked){
                binding?.bookmarkUser?.setImageResource(R.drawable.ic_bookmarked_white)
                this.isBookmarked = true
            } else{
                binding?.bookmarkUser?.setImageResource(R.drawable.ic_bookmark_border_white)
                this.isBookmarked = false
            }
        })

        binding?.apply {
            namaUser.text = user.name
            followersUser.text = user.followers.toString()
            followingUser.text = user.following.toString()
            usernameUser.text = getString(R.string.username_tag, user.login)
            locationUser.text = getString(R.string.location_tag, user.location)
            companyUser.text = getString(R.string.company_tag, user.company)
            repositoryUser.text = user.publicRepos.toString()
        }
        binding?.photoUser?.let {
            Glide.with(this)
                .load(user.avatarUrl)
                .into(it)
        }

        binding?.bookmarkUser?.setOnClickListener {
            if (isBookmarked){
                userBookmarkViewModel.delete(User(user.login,user.name,user.location, user.company, user.followers, user.following, user.avatarUrl, user.publicRepos, false))
                Toast.makeText(this, "Data $USERNAME Terhapus dari Favorit", Toast.LENGTH_SHORT).show()
            } else{
                userBookmarkViewModel.insert(User(user.login,user.name,user.location, user.company, user.followers, user.following, user.avatarUrl, user.publicRepos, true))
                Toast.makeText(this, "Data $USERNAME Tersimpan sebagai Favorit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMoveWithData = null
    }

    companion object{
        const val EXTRA_USER = "extra_user"
        const val EXTRA_USER_BOOKMARK = "extra_user_bookmark"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        var USERNAME: String = "username"
    }
}