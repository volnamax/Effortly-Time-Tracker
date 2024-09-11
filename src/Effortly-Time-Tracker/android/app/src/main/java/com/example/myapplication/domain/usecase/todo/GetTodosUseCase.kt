package com.example.myapplication.domain.usecase.todo
import com.example.myapplication.domain.repository.TodoRepository

class GetTodosUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(userId: Int) = todoRepository.getTodosByUserId(userId)
}