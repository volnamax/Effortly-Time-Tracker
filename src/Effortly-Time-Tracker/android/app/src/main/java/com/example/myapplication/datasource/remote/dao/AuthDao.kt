package com.example.myapplication.datasource.remote.dao

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthDao {
    @POST("/api/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<TokenResponse>

    @POST("/api/login")
    suspend fun login(@Body request: RegisterRequestDto): Response<TokenResponse>
}