package com.example.myapplication.datasource.remote.mapper
import com.example.myapplication.datasource.remote.model.TodoNodeResponseDTO
import com.example.myapplication.domain.model.Priority
import com.example.myapplication.domain.model.Todo

fun TodoNodeResponseDTO.toDomain(): Todo {
    return Todo(
        id = this.id,
        content = this.content,
        status = this.status,
        priority = when (this.priority) {
            "IMPORTANT_URGENTLY" -> Priority.IMPORTANT_URGENTLY
            "NO_IMPORTANT_URGENTLY" -> Priority.NO_IMPORTANT_URGENTLY
            "IMPORTANT_NO_URGENTLY" -> Priority.IMPORTANT_NO_URGENTLY
            else -> Priority.NO_IMPORTANT_NO_URGENTLY
        }
    )
}