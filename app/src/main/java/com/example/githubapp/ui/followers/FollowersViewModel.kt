package com.example.githubapp.ui.followers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.AccountRepository
import com.example.githubapp.data.Result
import com.example.githubapp.data.remote.response.ItemsItem
import kotlinx.coroutines.launch


class FollowersViewModel(private val accountRepository: AccountRepository) : ViewModel() {
    private val _listFollower = MutableLiveData<Result<List<ItemsItem>>>()
    val listFollower: LiveData<Result<List<ItemsItem>>> = _listFollower

    private val _listFollowing = MutableLiveData<Result<List<ItemsItem>>>()
    val listFollowing: LiveData<Result<List<ItemsItem>>> = _listFollowing

    fun findFollower(username: String) {
        viewModelScope.launch {
            accountRepository.getFollower(username).collect() {
                _listFollower.postValue(it)
            }
        }
    }

    fun findFollowing(username: String) {
        viewModelScope.launch {
            accountRepository.getFollowing(username).collect() {
                _listFollowing.postValue(it)
            }
        }
    }

    companion object {
        const val TAG = "FollowersViewModel"
        const val USERNAME_GITHUB = "guntur"
    }
}