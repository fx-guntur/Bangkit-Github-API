package com.example.githubapp.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubapp.data.AccountRepository
import com.example.githubapp.di.Injection
import com.example.githubapp.ui.dashboard.DashboardViewModel
import com.example.githubapp.ui.detail.DetailViewModel
import com.example.githubapp.ui.followers.FollowersViewModel
import com.example.githubapp.ui.setting.SettingPreferences
import com.example.githubapp.ui.setting.SettingViewModel

class ViewModelFactory private constructor(
    private val accountRepository: AccountRepository,
    private val pref: SettingPreferences
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(accountRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(accountRepository) as T
        } else if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(accountRepository) as T
        } else if (modelClass.isAssignableFrom(FollowersViewModel::class.java)) {
            return FollowersViewModel(accountRepository) as T
        } else if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideSettingPreferences(context)
                )
            }.also { instance = it }
    }
}