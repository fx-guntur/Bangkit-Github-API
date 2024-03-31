package com.example.githubapp.data.remote.retrofit

import com.example.githubapp.data.remote.response.DetailUserResponse
import com.example.githubapp.data.remote.response.GithubResponse
import com.example.githubapp.data.remote.response.ItemsItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    suspend fun getAccount(
        @Query("q") query: String
    ): GithubResponse

    @GET("/users/{user}")
    suspend fun getDetail(
        @Path("user") user: String
    ): DetailUserResponse

    @GET("/users/{user}/followers")
    suspend fun getFollower(
        @Path("user") user: String
    ): List<ItemsItem>

    @GET("users/{user}/following")
    suspend fun getFollowing(
        @Path("user") user: String
    ): List<ItemsItem>
}