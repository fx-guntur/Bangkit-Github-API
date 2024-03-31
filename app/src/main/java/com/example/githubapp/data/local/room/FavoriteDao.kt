package com.example.githubapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.githubapp.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite ORDER BY username DESC")
    fun getAcc(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite WHERE username = :username")
    suspend fun getUser(username: String): FavoriteEntity

    @Query("SELECT * FROM favorite where favorited = 1")
    fun getFavoriteAcc(): List<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFav(news: List<FavoriteEntity>)

    @Update
    suspend fun updateFav(news: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE favorited = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE username = :username AND favorited = 1)")
    suspend fun isAccFavorite(username: String): Boolean
}