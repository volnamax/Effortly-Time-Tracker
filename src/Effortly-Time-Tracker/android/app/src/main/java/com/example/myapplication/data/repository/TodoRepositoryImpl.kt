package com.example.myapplication.data.repository

import com.example.myapplication.data.datasourse.remote.sevice.TodoService
import com.example.myapplication.datasource.remote.mapper.toDomain
import com.example.myapplication.domain.model.Todo
import com.example.myapplication.domain.repository.TodoRepository

class TodoRepositoryImpl(private val todoService: TodoService) : TodoRepository {
    override suspend fun getTodosByUserId(userId: Int): List<Todo> {
        return todoService.getTodosByUserId(userId).map { it.toDomain() }
    }
    override suspend fun addTodo(todo: Todo, userId: Int) {
        todoService.addTodo(todo.content, todo.priority.name, userId)
    }
}