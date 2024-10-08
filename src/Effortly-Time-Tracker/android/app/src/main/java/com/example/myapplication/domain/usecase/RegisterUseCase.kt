package com.example.myapplication.domain.usecase

import com.example.myapplication.datasource.remote.model.LoginRequestDto
import com.example.myapplication.datasource.remote.model.RegisterRequestDto
import com.example.myapplication.datasource.remote.model.TokenResponse
import com.example.myapplication.domain.repository.AuthRepository


class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(request: RegisterRequestDto): Result<TokenResponse> {
        return authRepository.register(request)
    }
}

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(request: LoginRequestDto): Result<TokenResponse> {
        return authRepository.login(request)
    }
}