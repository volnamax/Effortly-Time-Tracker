package com.example.myapplication.data.repository

import com.example.myapplication.data.datasourse.remote.sevice.TodoService
import com.example.myapplication.datasource.remote.mapper.toDomain
import com.example.myapplication.datasource.remote.model.StatusEnum
import com.example.myapplication.datasource.remote.model.TodoNodeDTO
import com.example.myapplication.datasource.remote.model.TodoNodeResponseDTO
import com.example.myapplication.domain.model.Todo
import com.example.myapplication.domain.repository.TodoRepository

class TodoRepositoryImpl(private val todoService: TodoService) : TodoRepository {
    override suspend fun getTodosByUserId(userId: Int): List<Todo> {
        return todoService.getTodosByUserId(userId).map { it.toDomain() }
    }
    override suspend fun addTodo(todoNodeDTO: TodoNodeDTO) {
        todoService.addTodo(todoNodeDTO)
    }
    override suspend fun updateTodoStatus(id: Long, status: StatusEnum): TodoNodeResponseDTO {
        return todoService.updateTodoStatus(id, status)
    }
}