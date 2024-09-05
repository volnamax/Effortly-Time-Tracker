package com.example.myapplication.datasource.remote.model

data class TokenResponse(
    val token: String,
    val user: UserResponseDTO
)

data class UserResponseDTO(
    val id: Long,
    val email: String,
    val role: String,
    val userName: String,  // Изменено для соответствия серверному ответу
    val userSecondname: String  // Изменено для соответствия серверному ответу
)