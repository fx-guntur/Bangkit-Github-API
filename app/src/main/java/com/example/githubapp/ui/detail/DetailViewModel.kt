package com.example.githubapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.AccountRepository
import com.example.githubapp.data.Result
import com.example.githubapp.data.local.entity.FavoriteEntity
import kotlinx.coroutines.launch

class DetailViewModel(private val accountRepository: AccountRepository) : ViewModel() {
    private val _detailUser = MutableLiveData<com.example.githubapp.data.Result<FavoriteEntity>>()
    val detailUser: LiveData<Result<FavoriteEntity>> = _detailUser

    fun findDetail(username: String) {
        viewModelScope.launch {
            accountRepository.getDetail(username).collect() {
                _detailUser.postValue(it)
            }
        }
    }

    fun saveFav(news: FavoriteEntity, isFav: Boolean) {
        viewModelScope.launch {
            accountRepository.setFavoriteAcc(news, isFav)
        }
    }

//    fun isFav(data: FavoriteEntity, username: String) : Boolean {
//
//    }
}