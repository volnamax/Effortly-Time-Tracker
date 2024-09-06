package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.User

interface UserRepository {
    suspend fun getUserByEmail(email: String): User
}