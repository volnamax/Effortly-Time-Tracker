package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Todo

interface TodoRepository {
    suspend fun getTodosByUserId(userId: Int): List<Todo>
    suspend fun addTodo(todo: Todo, userId: Int)

}