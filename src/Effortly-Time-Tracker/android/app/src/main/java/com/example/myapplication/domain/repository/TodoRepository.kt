package com.example.myapplication.domain.repository

import com.example.myapplication.datasource.remote.model.StatusEnum
import com.example.myapplication.datasource.remote.model.TodoNodeDTO
import com.example.myapplication.datasource.remote.model.TodoNodeResponseDTO
import com.example.myapplication.domain.model.Todo

interface TodoRepository {
    suspend fun getTodosByUserId(userId: Int): List<Todo>
    suspend fun addTodo(todo: TodoNodeDTO)
    suspend fun updateTodoStatus(id: Long, status: StatusEnum): TodoNodeResponseDTO
}