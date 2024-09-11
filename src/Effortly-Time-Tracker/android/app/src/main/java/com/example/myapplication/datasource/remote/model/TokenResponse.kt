package com.example.myapplication.datasource.remote.model


data class TokenResponse(
    val token: String,
    val user: UserResponseDTO
)

data class UserResponseDTO(
    val userId: Long,
    val email: String,
    val role: String,
    val userName: String,
    val userSecondname: String
)