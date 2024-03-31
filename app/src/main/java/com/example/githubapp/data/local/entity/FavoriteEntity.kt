package com.example.githubapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
class FavoriteEntity(
    @field:ColumnInfo(name = "username")
    @PrimaryKey(autoGenerate = false)
    var username: String = "",

    @field:ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null,

    @field:ColumnInfo(name = "totalFollower")
    var totalFollower: Int? = null,

    @field:ColumnInfo(name = "totalFollowing")
    var totalFollowing: Int? = null,

    @field:ColumnInfo(name = "fullName")
    var fullName: String? = null,

    @field:ColumnInfo(name = "favorited")
    var isFavorited: Boolean
)