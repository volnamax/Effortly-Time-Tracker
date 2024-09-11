package com.example.myapplication.presentation.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState

    fun getUserByEmail(email: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserByEmail(email)

                _userState.value = user
            } catch (e: Exception) {
                Log.e("APP", "Error  ${e.message}")
            }
        }
    }
}
