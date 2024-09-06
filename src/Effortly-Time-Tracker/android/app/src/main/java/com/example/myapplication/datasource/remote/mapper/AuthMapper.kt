package com.example.myapplication.datasource.remote.mapper


import com.example.myapplication.datasource.remote.model.UserResponseDTO
import com.example.myapplication.domain.model.User

fun UserResponseDTO.toDomain(): User {
    return User(
        userId = this.userId,
        userName = this.userName,
        userSecondname = this.userSecondname,
        email = this.email,
        role = this.role
    )
}