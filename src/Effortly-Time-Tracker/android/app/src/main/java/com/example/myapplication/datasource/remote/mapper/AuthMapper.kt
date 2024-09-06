package com.example.myapplication.datasource.remote.mapper


import com.example.myapplication.datasource.remote.model.UserResponseDTO
import com.example.myapplication.domain.model.User

fun UserResponseDTO.toDomain(): User {
    return User(
        id = this.id,
        firstName = this.userName,
        lastName = this.userSecondname,
        email = this.email,
        role = this.role
    )
}