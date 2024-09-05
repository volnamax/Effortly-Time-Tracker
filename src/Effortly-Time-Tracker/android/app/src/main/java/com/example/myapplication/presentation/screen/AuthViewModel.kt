package com.example.myapplication.presentation.screen


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.datasource.remote.model.LoginRequestDto
import com.example.myapplication.datasource.remote.model.RegisterRequestDto
import com.example.myapplication.domain.usecase.LoginUseCase
import com.example.myapplication.domain.usecase.RegisterUseCase
import com.example.myapplication.presentation.mapper.toPresentation
import com.example.myapplication.presentation.model.UserPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    // Presentation state to hold UserPresentation data
    private val _loginState = MutableStateFlow<UserPresentation?>(null)
    val loginState: StateFlow<UserPresentation?> = _loginState

    private val _registerState = MutableStateFlow<UserPresentation?>(null)
    val registerState: StateFlow<UserPresentation?> = _registerState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val request = LoginRequestDto(login = email, password = password)
            val result = loginUseCase(request)
            result.onSuccess {
                // Преобразуем доменную модель User в модель уровня presentation
                val userPresentation = it.toPresentation()
                _loginState.value = userPresentation
                Log.d("AuthViewModel", "Login successful: User - ${userPresentation.displayName}")
            }.onFailure {
                Log.e("AuthViewModel", "Login failed: ${it.message}")
            }
        }
    }

    fun register(userName: String, userSecondName: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            val request = RegisterRequestDto(userName, userSecondName, email, password, role)
            val result = registerUseCase(request)
            result.onSuccess {
                // Преобразуем доменную модель User в модель уровня presentation
                val userPresentation = it.toPresentation()
                _registerState.value = userPresentation
                Log.d("AuthViewModel", "Registration successful: User - ${userPresentation.displayName}")
            }.onFailure {
                Log.e("AuthViewModel", "Registration failed: ${it.message}")
            }
        }
    }
}