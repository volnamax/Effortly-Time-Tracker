package com.example.myapplication.presentation.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.myapplication.AuthService
import com.example.myapplication.LoginRequestDto
import com.example.myapplication.RegisterRequestDto
import com.example.myapplication.TokenResponse

class AuthViewModel(private val authService: AuthService) : ViewModel() {


    private val _loginState = mutableStateOf<TokenResponse?>(null)
    val loginState: State<TokenResponse?> = _loginState

    private val _registerState = mutableStateOf<TokenResponse?>(null)
    val registerState: State<TokenResponse?> = _registerState

    fun login(loginRequest: LoginRequestDto) {
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "Attempting login with: $loginRequest")
                val response = authService.login(loginRequest)
                if (response.isSuccessful) {
                    _loginState.value = response.body()
                    Log.d("AuthViewModel", "Login successful, token: ${response.body()?.token}")
                } else {
                    Log.e("AuthViewModel", "Login failed with error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login error: ${e.localizedMessage}", e)
            }
        }
    }

    fun register(registerRequest: RegisterRequestDto) {
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "Attempting registration with: $registerRequest")
                val response = authService.register(registerRequest)
                if (response.isSuccessful) {
                    _registerState.value = response.body()
                    Log.d("AuthViewModel", "Registration successful, token: ${response.body()?.token}")
                } else {
                    Log.e("AuthViewModel", "Registration failed with error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration error: ${e.localizedMessage}", e)
            }
        }
    }
    class Factory(private val authService: AuthService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(authService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
