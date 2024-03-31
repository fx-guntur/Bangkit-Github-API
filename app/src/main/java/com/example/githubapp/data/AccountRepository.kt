package com.example.githubapp.data

import com.example.githubapp.data.local.entity.FavoriteEntity
import com.example.githubapp.data.local.room.FavoriteDao
import com.example.githubapp.data.remote.response.GithubResponse
import com.example.githubapp.data.remote.response.ItemsItem
import com.example.githubapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AccountRepository private constructor(
    private val apiService: ApiService,
    private val favDao: FavoriteDao,
) {
    fun getAccount(username: String): Flow<Result<GithubResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getAccount(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetail(username: String): Flow<Result<FavoriteEntity>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getDetail(username)
            val newUserList = ArrayList<FavoriteEntity>()
            val isFavorite = favDao.isAccFavorite(response.login)
            val favoriteEntity = FavoriteEntity(
                response.login,
                response.avatarUrl,
                response.followers,
                response.following,
                response.name,
                isFavorite
            )
            newUserList.add(favoriteEntity)

            favDao.deleteAll()
            favDao.insertFav(newUserList)
            val user = favDao.getUser(response.login)

            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFollower(username: String): Flow<Result<List<ItemsItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getFollower(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFollowing(username: String): Flow<Result<List<ItemsItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowing(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun getFavoriteAcc(): Flow<List<FavoriteEntity>> = flow {
        emit(favDao.getFavoriteAcc())
    }

    suspend fun setFavoriteAcc(fav: FavoriteEntity, favoriteState: Boolean) {
        fav.isFavorited = favoriteState
        favDao.updateFav(fav)
    }

    companion object {
        @Volatile
        private var instance: AccountRepository? = null

        private const val TAG = "AccountRepository"
        fun getInstance(
            apiService: ApiService,
            favDao: FavoriteDao,
        ): AccountRepository =
            instance ?: synchronized(this) {
                instance ?: AccountRepository(apiService, favDao)
            }.also { instance = it }
    }
}