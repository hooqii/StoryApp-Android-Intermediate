package com.example.storyapp.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Result
import com.example.storyapp.data.remote.repository.StoryRepository
import com.example.storyapp.data.remote.response.ErrorResponse

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun register(nameInput: String, emailInput: String, passwordInput: String): LiveData<Result<ErrorResponse>> =
        storyRepository.register(nameInput, emailInput, passwordInput)
}