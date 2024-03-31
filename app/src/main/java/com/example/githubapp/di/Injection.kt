package com.example.githubapp.di

import android.content.Context
import com.example.githubapp.data.AccountRepository
import com.example.githubapp.data.local.room.FavoriteDatabase
import com.example.githubapp.data.remote.retrofit.ApiConfig
import com.example.githubapp.ui.setting.SettingPreferences

object Injection {
    fun provideRepository(context: Context): AccountRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.favDao()
        return AccountRepository.getInstance(apiService, dao)
    }

    fun provideSettingPreferences(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context)
    }
}