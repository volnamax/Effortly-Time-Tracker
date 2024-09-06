package com.example.myapplication.domain.usecase.todo

import com.example.myapplication.domain.model.Todo
import com.example.myapplication.domain.repository.TodoRepository

class AddTodoUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(todo: Todo, userId: Int) {
        todoRepository.addTodo(todo, userId)
    }
}
