package com.example.myapplication.domain.model

data class Todo(
    val id: Long,
    val content: String,
    val status: String,
    val priority: Priority
)

enum class Priority {
    IMPORTANT_URGENTLY, NO_IMPORTANT_URGENTLY, IMPORTANT_NO_URGENTLY, NO_IMPORTANT_NO_URGENTLY
}