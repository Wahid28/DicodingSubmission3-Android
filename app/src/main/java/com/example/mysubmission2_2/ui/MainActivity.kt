package com.example.mysubmission2_2.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission2_2.*
import com.example.mysubmission2_2.databinding.ActivityMainBinding
import com.example.mysubmission2_2.remote.response.ResponseUserDetail
import com.example.mysubmission2_2.remote.response.ResponseUserSearch
import com.example.mysubmission2_2.remote.response.UserResponse
import com.example.mysubmission2_2.remote.retrofit.ApiConfig
import com.example.mysubmission2_2.ui.adapter.ListUserAdapter
import com.example.mysubmission2_2.ui.factory.ViewModelFactory
import com.example.mysubmission2_2.ui.viewmodel.MainViewModel
import com.example.mysubmission2_2.ui.viewmodel.SettingsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity(), ListUserAdapter.OnItemClickCallback {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding

    private val list = ArrayList<ResponseUserDetail>()
    private val mainViewModel by viewModels<MainViewModel>()

    //Memulai Main Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Users"

        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModelTheme = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[SettingsViewModel::class.java]

        mainViewModelTheme.getThemeSettings().observe(this,
            {isDarkModeActive: Boolean ->
                if (isDarkModeActive){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })

        mainViewModel.dataUser.observe(this, {dataUser ->
            getDetailUser(dataUser)
        })

        mainViewModel.isLoading.observe(this, {showLoading(it)})

        showRecyclerList()
    }

    //Membuat Option Menu & Fungsi Option Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(username: String): Boolean {
                searchUser(username)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.settings -> {
                val intent = Intent(this, ThemeSetting::class.java)
                startActivity(intent)
                true
            }
            R.id.bookmark -> {
                val intent = Intent(this, BookmarkActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    //Menampilkan Recycler View
    private fun showRecyclerList(){
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            binding?.rvUser?.layoutManager = GridLayoutManager(this, 2)
        } else{
            binding?.rvUser?.layoutManager = LinearLayoutManager(this)
        }

        val listUserAdapter = ListUserAdapter(list, this)
        binding?.rvUser?.adapter = listUserAdapter
    }

    //Mencari User dari Username
    private fun searchUser(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().searchUser(username)
        client.enqueue(object : Callback<ResponseUserSearch> {
            override fun onResponse(
                call: Call<ResponseUserSearch>,
                response: Response<ResponseUserSearch>
            ){
                showLoading(false)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        getDetailUser(responseBody.items)
                        Toast.makeText(this@MainActivity, "Data Ditemukan", Toast.LENGTH_SHORT).show()
//                        client.cancel()
                    }
                } else{
                    Log.e(TAG,"onFailure: ${response.message()}")
                    Toast.makeText(this@MainActivity, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseUserSearch>, t: Throwable){
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(this@MainActivity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //Menentukan detail user mana yang akan diambil
     private fun getDetailUser(user: ArrayList<UserResponse>){
        val listDetailUser = arrayListOf<ResponseUserDetail>()
        for((index, data) in user.withIndex()){
            val client = ApiConfig.getApiService().getUser(data.login)
            client.enqueue(object : Callback<ResponseUserDetail> {
                override fun onResponse(
                    call: Call<ResponseUserDetail>,
                    response: Response<ResponseUserDetail>
                ){
                    showLoading(false)
                    if (response.isSuccessful){
                        val responseBody = response.body()
                        if(responseBody != null){
                            listDetailUser.add(responseBody)
                            if(index == user.size - 1){
                                val adapter = ListUserAdapter(listDetailUser, this@MainActivity)
                                binding?.rvUser?.adapter = adapter
//                                client.cancel()
                            }
                        }
                    } else{
                        Log.e(TAG,"onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<ResponseUserDetail>, t: Throwable){
                    showLoading(false)
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    //Menampilkan & menghapuskan Loading
    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    //Fungsi untuk apabila diklik akan masuk ke halaman detail
    override fun onItemClicked(data: ResponseUserDetail) {
        val intentToDetail = Intent(this@MainActivity, MoveDataWithActivity::class.java)
        intentToDetail.putExtra(MoveDataWithActivity.EXTRA_USER, data)
        startActivity(intentToDetail)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }

    //Object yang akan diambil
    companion object {
        private const val TAG = "MainActivity"
    }



}