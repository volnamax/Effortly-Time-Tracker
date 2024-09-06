package com.example.myapplication.datasource.remote.mapper
import com.example.myapplication.datasource.remote.model.StatusEnum
import com.example.myapplication.datasource.remote.model.TodoNodeResponseDTO
import com.example.myapplication.domain.model.Priority
import com.example.myapplication.domain.model.Todo

fun TodoNodeResponseDTO.toDomain(): Todo {
    return Todo(
        todoId = this.todoId,
        content = this.content,
        status = when(this.status){
            "NO_ACTIVE" -> StatusEnum.NO_ACTIVE
            else -> StatusEnum.ACTIVE
        },
        priority = when (this.priority) {
            "IMPORTANT_URGENTLY" -> Priority.IMPORTANT_URGENTLY
            "NO_IMPORTANT_URGENTLY" -> Priority.NO_IMPORTANT_URGENTLY
            "IMPORTANT_NO_URGENTLY" -> Priority.IMPORTANT_NO_URGENTLY
            else -> Priority.NO_IMPORTANT_NO_URGENTLY
        }
    )
}