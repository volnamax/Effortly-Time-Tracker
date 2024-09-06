package com.example.myapplication.domain.model

import com.example.myapplication.datasource.remote.model.StatusEnum

data class Todo(
    val id_todo: Long,
    val content: String,
    val status: StatusEnum,
    val priority: Priority
)

enum class Priority {
    IMPORTANT_URGENTLY, NO_IMPORTANT_URGENTLY, IMPORTANT_NO_URGENTLY, NO_IMPORTANT_NO_URGENTLY
}