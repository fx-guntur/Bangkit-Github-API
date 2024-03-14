package com.example.githubapp.data.retrofit

import com.example.githubapp.data.response.DetailUserResponse
import com.example.githubapp.data.response.GithubResponse
import com.example.githubapp.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    fun getAccount(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("/users/{user}")
    fun getDetail(
        @Path("user") user: String
    ): Call<DetailUserResponse>

    @GET("/users/{user}/followers")
    fun getFollower(
        @Path("user") user: String
    ): Call<List<ItemsItem>>

    @GET("users/{user}/following")
    fun getFollowing(
        @Path("user") user: String
    ): Call<List<ItemsItem>>
}