package com.example.myapplication.datasource.remote.model

data class RegisterRequestDto(
    val userName: String,
    val userSecondname: String,
    val email: String,
    val passwordHash: String,
    val role: String
)
data class LoginRequestDto(
    val login: String,
    val password: String
)