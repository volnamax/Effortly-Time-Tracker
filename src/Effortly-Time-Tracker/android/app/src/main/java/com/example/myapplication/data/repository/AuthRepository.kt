package com.example.myapplication.data.repository

import com.example.myapplication.data.datasourse.remote.sevice.AuthService
import com.example.myapplication.datasource.remote.mapper.toDomain
import com.example.myapplication.datasource.remote.model.LoginRequestDto
import com.example.myapplication.datasource.remote.model.RegisterRequestDto
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.AuthRepository

class AuthRepositoryImpl(private val authService: AuthService) : AuthRepository {

    override suspend fun login(request: LoginRequestDto): Result<User> {
        return try {
            val response = authService.login(request)
            if (response.isSuccessful) {
                response.body()?.let { tokenResponse ->
                    // Преобразуем данные из UserResponseDTO в доменную модель User
                    Result.success(tokenResponse.user.toDomain())
                } ?: Result.failure(Exception("No response body"))
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequestDto): Result<User> {
        return try {
            val response = authService.register(request)
            if (response.isSuccessful) {
                response.body()?.let { tokenResponse ->
                    // Преобразуем данные из UserResponseDTO в доменную модель User
                    Result.success(tokenResponse.user.toDomain())
                } ?: Result.failure(Exception("No response body"))
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
