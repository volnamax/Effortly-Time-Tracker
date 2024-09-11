package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.datasourse.remote.sevice.UserService
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.UserRepository

class UserRepositoryImpl(private val userService: UserService) : UserRepository {
    override suspend fun getUserByEmail(email: String): User {
        // Выполняем запрос к API
        val userResponse = userService.getUserByEmail(email)

        // Логируем ответ от сервера
        Log.e("UserRepository", "Server response: $userResponse")

        return userResponse    }
}
