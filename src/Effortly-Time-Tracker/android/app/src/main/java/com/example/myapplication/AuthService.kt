package com.example.myapplication

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

data class LoginRequestDto(val login: String, val password: String)
data class RegisterRequestDto(
    val userName: String,
    val userSecondname: String,
    val email: String,
    val passwordHash: String,
    val role: String
)
data class TokenResponse(val token: String, val user: UserResponseDTO)
data class UserResponseDTO(val id: Long, val email: String, val role: String)

interface AuthService {

    @POST("/api/login")
    suspend fun login(@Body request: LoginRequestDto): Response<TokenResponse>

    @POST("/api/register")
    suspend fun register(@Body request: RegisterRequestDo): Response<TokenResponse>
}
