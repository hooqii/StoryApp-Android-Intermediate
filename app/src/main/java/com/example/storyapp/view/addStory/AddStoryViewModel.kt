package com.example.storyapp.view.addStory

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.repository.StoryRepository
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun uploadStory(getFile: File?, description: String, lat: Float? = null, lon: Float? = null) =
        storyRepository.uploadStory(getFile, description, lat, lon)
}