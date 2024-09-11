package com.example.myapplication.data.datasourse.remote.sevice

import com.example.myapplication.domain.model.User
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/api/user/get-by-email")
    suspend fun getUserByEmail(@Query("email") email: String): User
}