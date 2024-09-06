package com.example.myapplication.presentation.screen.auth


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.context.TokenManager
import com.example.myapplication.context.TokenManager.getUserIdFromToken
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
                TokenManager.saveToken(context, tokenResponse.token, userId = -1) // Сохраняем токен с временным userId

                // Извлекаем userId из токена (по email) и сохраняем
                TokenManager.getUserIdFromToken(tokenResponse.token, context) { userId ->
                    if (userId != null) {
                        // Обновляем сохранённый токен и userId
                        TokenManager.saveToken(context, tokenResponse.token, userId)
                        Log.d("AuthViewModel", "Login successful: Token saved - ${tokenResponse.token}, User ID - $userId")

                        // Переходим на главный экран
                        navController.navigate("todo") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        Log.e("AuthViewModel", "Failed to retrieve User ID")
                    }
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
                TokenManager.saveToken(context, tokenResponse.token, userId = -1) // Сохраняем токен с временным userId

                // Извлекаем userId из токена (по email) и сохраняем
                TokenManager.getUserIdFromToken(tokenResponse.token, context) { userId ->
                    if (userId != null) {
                        // Обновляем сохранённый токен и userId
                        TokenManager.saveToken(context, tokenResponse.token, userId)
                        Log.d("AuthViewModel", "Registration successful: User - ${tokenResponse.user.email}, User ID - $userId")

                        // Переходим на главный экран
                        navController.navigate("todo") {
                            popUpTo("register") { inclusive = true }
                        }
                    } else {
                        Log.e("AuthViewModel", "Failed to retrieve User ID")
                    }
                }
            }.onFailure {
                Log.e("AuthViewModel", "Registration failed: ${it.message}")
            }
        }
    }


}
