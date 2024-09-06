package com.example.myapplication.presentation.screen


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.context.TokenManager
import com.example.myapplication.datasource.remote.model.LoginRequestDto
import com.example.myapplication.datasource.remote.model.RegisterRequestDto
import com.example.myapplication.domain.usecase.LoginUseCase
import com.example.myapplication.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch


class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    fun login(email: String, password: String, context: Context, navController: NavController) {
        viewModelScope.launch {
            val request = LoginRequestDto(login = email, password = password)
            val result = loginUseCase(request)
            result.onSuccess { tokenResponse ->
                // Сохраняем JWT токен
                TokenManager.saveToken(context, tokenResponse.token)
                val savedToken = TokenManager.getToken(context)
                Log.d("AuthViewModel", "Login successful: Token saved - $savedToken")

                Log.d("AuthViewModel", "Login successful: User - ${tokenResponse.user.email}")

                // Переходим на главный экран
                navController.navigate("main_screen") {
                    popUpTo("login") { inclusive = true }  // Удаляем из стека экран логина
                }
            }.onFailure {
                Log.e("AuthViewModel", "Login failed: ${it.message}")
            }
        }
    }

    fun register(
        userName: String,
        userSecondName: String,
        email: String,
        password: String,
        role: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch {
            val request = RegisterRequestDto(userName, userSecondName, email, password, role)
            val result = registerUseCase(request)
            result.onSuccess { tokenResponse ->
                // Сохраняем JWT токен
                TokenManager.saveToken(context, tokenResponse.token)
                Log.d(
                    "AuthViewModel",
                    "Registration successful: User - ${tokenResponse.user.email}"
                )

                val savedToken = TokenManager.getToken(context)
                Log.d("AuthViewModel", "Registration successful: Token saved - $savedToken")

                // Переходим на главный экран
                navController.navigate("main_screen") {
                    popUpTo("register") { inclusive = true }  // Удаляем из стека экран регистрации
                }
            }.onFailure {
                Log.e("AuthViewModel", "Registration failed: ${it.message}")
            }
        }
    }
}