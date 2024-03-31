package com.example.githubapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.AccountRepository
import com.example.githubapp.data.Result
import com.example.githubapp.data.remote.response.GithubResponse
import kotlinx.coroutines.launch

class DashboardViewModel(private val accountRepository: AccountRepository) : ViewModel() {
    private val _findUser = MutableLiveData<Result<GithubResponse>>()
    val findUser: LiveData<Result<GithubResponse>> = _findUser

    init {
        findAccount(USERNAME_GITHUB)
    }

    fun findAccount(username: String) {
        viewModelScope.launch {
            accountRepository.getAccount(username).collect() {
                _findUser.postValue(it)
            }
        }
    }

    companion object {
        const val TAG = "DashboardViewModel"
        const val USERNAME_GITHUB = "guntur"
    }
}