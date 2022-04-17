package com.example.mysubmission2_2.remote.retrofit


import com.example.mysubmission2_2.BuildConfig
import com.example.mysubmission2_2.remote.response.ResponseUserDetail
import com.example.mysubmission2_2.remote.response.ResponseUserSearch
import com.example.mysubmission2_2.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users/{username}")
    @Headers("Authorization: token $API_TOKEN")
    fun getUser(
        @Path("username") username: String
    ): Call<ResponseUserDetail>

    @GET("search/users")
    @Headers("Authorization: token $API_TOKEN")
    fun searchUser(
        @Query("q") username: String
    ): Call<ResponseUserSearch>

    @GET("users/{username}/following")
    @Headers("Authorization: token $API_TOKEN")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<UserResponse>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $API_TOKEN")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<UserResponse>>

    companion object{
        private const val API_TOKEN: String = BuildConfig.MyGithubAPIToken
    }
}