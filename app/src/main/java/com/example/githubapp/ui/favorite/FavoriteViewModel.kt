package com.example.githubapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.AccountRepository
import com.example.githubapp.data.local.entity.FavoriteEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val accountRepository: AccountRepository) : ViewModel() {
    private val _favoriteUser = MutableLiveData<List<FavoriteEntity>>()
    val favoriteUser: LiveData<List<FavoriteEntity>> = _favoriteUser

    fun getFavoriteAccount() {
        viewModelScope.launch {
            accountRepository.getFavoriteAcc().collect() {
                _favoriteUser.postValue(it)
            }
        }
    }

    companion object {
        const val TAG = "FollowersViewModel"
        const val USERNAME_GITHUB = "guntur"
    }
}