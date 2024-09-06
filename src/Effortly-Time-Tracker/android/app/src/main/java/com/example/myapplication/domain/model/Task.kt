package com.example.myapplication.domain.model

data class Task(
    val taskId: Int,
    val name: String,
    val description: String?,
    val status: String,
    val tableId: Int
)
