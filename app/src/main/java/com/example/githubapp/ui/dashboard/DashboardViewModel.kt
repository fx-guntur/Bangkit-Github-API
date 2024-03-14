package com.example.githubapp.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapp.data.response.GithubResponse
import com.example.githubapp.data.response.ItemsItem
import com.example.githubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {
    private val _findUser = MutableLiveData<GithubResponse>()
    val findUser: LiveData<GithubResponse> = _findUser

    private val _listAccount = MutableLiveData<List<ItemsItem>>()
    val listAccount: LiveData<List<ItemsItem>> = _listAccount

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    init {
        findAccount(USERNAME_GITHUB)
    }

    fun findAccount(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAccount(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody.also { result ->
                        _findUser.value = result
                        _listAccount.value = result?.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        const val TAG = "DashboardViewModel"
        const val USERNAME_GITHUB = "guntur"
    }
}