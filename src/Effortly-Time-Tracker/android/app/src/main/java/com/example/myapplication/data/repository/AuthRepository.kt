package com.example.myapplication.data.repository

import com.example.myapplication.data.datasourse.remote.sevice.AuthService
import com.example.myapplication.datasource.remote.model.LoginRequestDto
import com.example.myapplication.datasource.remote.model.RegisterRequestDto
import com.example.myapplication.datasource.remote.model.TokenResponse
import com.example.myapplication.domain.repository.AuthRepository

class AuthRepositoryImpl(private val authService: AuthService) : AuthRepository {

    override suspend fun login(request: LoginRequestDto): Result<TokenResponse> {
        return try {
            val response = authService.login(request)
            if (response.isSuccessful) {
                response.body()?.let { tokenResponse ->
                    Result.success(tokenResponse)
                } ?: Result.failure(Exception("No response body"))
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequestDto): Result<TokenResponse> {
        return try {
            val response = authService.register(request)
            if (response.isSuccessful) {
                response.body()?.let { tokenResponse ->
                    Result.success(tokenResponse)
                } ?: Result.failure(Exception("No response body"))
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}