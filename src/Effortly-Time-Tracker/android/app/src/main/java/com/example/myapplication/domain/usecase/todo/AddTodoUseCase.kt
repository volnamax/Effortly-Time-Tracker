package com.example.myapplication.domain.usecase.todo

import com.example.myapplication.datasource.remote.model.TodoNodeDTO
import com.example.myapplication.domain.repository.TodoRepository

class AddTodoUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(todoNodeDTO: TodoNodeDTO) {
        todoRepository.addTodo(todoNodeDTO)
    }
}
