package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.preferences.LoginPreferences
import com.example.storyapp.data.preferences.dataStore
import com.example.storyapp.data.local.StoryDatabase
import com.example.storyapp.data.remote.repository.StoryRepository
import com.example.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        val token = runBlocking {
            pref.getToken().first()
        }
        val apiService = ApiConfig.getApiService(token.toString())
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, pref, storyDatabase)
    }
}