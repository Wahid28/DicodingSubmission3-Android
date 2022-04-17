package com.example.mysubmission2_2.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User (
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "name")
    val name: String?,

    @field:ColumnInfo(name = "location")
    val location: String?,

    @field:ColumnInfo(name = "company")
    val company: String?,

    @field:ColumnInfo(name = "follower")
    val follower: Int,

    @field:ColumnInfo(name = "following")
    val following: Int,

    @field:ColumnInfo(name = "avatarUrl")
    val avatarUrl: String,

    @field:ColumnInfo(name = "publicRepos")
    val publicRepos: Int,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean
    ): Parcelable