package com.example.myapplication.presentation.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.datasource.remote.model.LoginRequestDto
import com.example.myapplication.datasource.remote.model.RegisterRequestDto
import com.example.myapplication.domain.usecase.LoginUseCase
import com.example.myapplication.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val request = LoginRequestDto(login = email, password = password)
            val result = loginUseCase(request)
            result.onSuccess {
                // Логирование успешного логина
                Log.d("AuthViewModel", "Login successful: User - ${it.email}, Role - ${it.role}")
            }.onFailure {
                // Логирование ошибки
                Log.e("AuthViewModel", "Login failed: ${it.message}")
            }
        }
    }

    fun register(userName: String, userSecondName: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            val request = RegisterRequestDto(userName, userSecondName, email, password, role)
            val result = registerUseCase(request)
            result.onSuccess {
                // Логирование успешной регистрации
                Log.d("AuthViewModel", "Registration successful: User - ${it.email}, Role - ${it.role}")
            }.onFailure {
                // Логирование ошибки
                Log.e("AuthViewModel", "Registration failed: ${it.message}")
            }
        }
    }
}
