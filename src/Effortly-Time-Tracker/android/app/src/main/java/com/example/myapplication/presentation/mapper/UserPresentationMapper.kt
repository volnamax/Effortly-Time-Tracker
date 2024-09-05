package com.example.myapplication.presentation.mapper

import com.example.myapplication.domain.model.User
import com.example.myapplication.presentation.model.UserPresentation


fun User.toPresentation(): UserPresentation {
    return UserPresentation(
        displayName = "$email (${role})",
        role = role
    )
}