package com.example.storyapp.view.maps

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.repository.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStoriesWithLocation() = storyRepository.getStoriesWithLocation()
}