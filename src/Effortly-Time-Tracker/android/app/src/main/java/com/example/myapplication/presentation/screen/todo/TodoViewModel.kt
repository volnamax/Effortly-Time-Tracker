package com.example.myapplication.presentation.screen.todo


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.datasource.remote.model.StatusEnum
import com.example.myapplication.datasource.remote.model.TodoNodeDTO
import com.example.myapplication.domain.usecase.todo.AddTodoUseCase
import com.example.myapplication.domain.usecase.todo.GetTodosUseCase
import com.example.myapplication.domain.usecase.todo.UpdateTodoStatusUseCase
import com.example.myapplication.presentation.mapper.toPresentation
import com.example.myapplication.presentation.model.TodoPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TodoViewModel(
    private val getTodosUseCase: GetTodosUseCase,
    private val addTodoUseCase: AddTodoUseCase, // Добавляем UseCase для добавления задачи
    private val updateTodoStatusUseCase: UpdateTodoStatusUseCase
) : ViewModel() {

    private val _todoState = MutableStateFlow<List<TodoPresentation>>(emptyList())
    val todoState: StateFlow<List<TodoPresentation>> = _todoState

    // Функция для загрузки всех задач пользователя
    fun loadTodos(userId: Int) {
        viewModelScope.launch {
            try {
                val todos = getTodosUseCase(userId)
                _todoState.value = todos.map { it.toPresentation() }
            } catch (e: Exception) {
                // todo Обрабатываем ошибку
            }
        }
    }

    // Функция для добавления новой задачи


    fun addTodo(content: String, priority: String, userId: Int) {
        viewModelScope.launch {
            try {
                val newTodoDTO = TodoNodeDTO(
                    content = content,
                    priority = priority,
                    userID = userId
                )
                Log.d("APP", "Adding new todo: $newTodoDTO")
                addTodoUseCase(newTodoDTO)  // Передаем DTO в UseCase
                loadTodos(userId)  // Обновляем список после добавления
            } catch (e: Exception) {
                Log.e("APP", "Error adding todo: ${e.message}")
            }
        }
    }

    fun updateTodoStatus(todoId: Long, newStatus: StatusEnum, userId: Int) {
        viewModelScope.launch {
            try {
                Log.d("APP", "todoId = $todoId,,,,,,${newStatus.toString()}")
                updateTodoStatusUseCase(todoId, newStatus)
                loadTodos(userId)
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }
}