package com.example.myapplication.domain.usecase.todo

import com.example.myapplication.datasource.remote.model.StatusEnum
import com.example.myapplication.domain.repository.TodoRepository

class UpdateTodoStatusUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(id: Long, status: StatusEnum) {
        todoRepository.updateTodoStatus(id, status)
    }
}
