package com.example.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.storyapp.data.Result
import com.example.storyapp.data.preferences.LoginPreferences
import com.example.storyapp.data.remote.repository.StoryRepository
import com.example.storyapp.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(
    storyRepository: StoryRepository,
    private val preferences: LoginPreferences
) : ViewModel() {

    val getStories: LiveData<Result<PagingData<ListStoryItem>>> by lazy {
        storyRepository.getAllStoriesWithPaging(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            preferences.logout()
        }
    }
}