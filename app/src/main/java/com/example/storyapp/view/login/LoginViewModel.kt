package com.example.storyapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.preferences.LoginPreferences
import com.example.storyapp.data.remote.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val storyRepository: StoryRepository,
    private val preferences: LoginPreferences
) : ViewModel() {
    fun login(emailInput: String, passwordInput: String) =
        storyRepository.login(emailInput, passwordInput)

    fun saveState(token: String) {
        viewModelScope.launch {
            preferences.saveToken(token)
            preferences.login()
        }
    }
}