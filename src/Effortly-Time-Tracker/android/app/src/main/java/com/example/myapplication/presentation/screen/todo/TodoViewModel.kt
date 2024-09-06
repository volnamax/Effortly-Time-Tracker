package com.example.myapplication.presentation.screen.todo


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Priority
import com.example.myapplication.domain.model.Todo
import com.example.myapplication.domain.usecase.todo.AddTodoUseCase
import com.example.myapplication.domain.usecase.todo.GetTodosUseCase
import com.example.myapplication.presentation.mapper.toPresentation
import com.example.myapplication.presentation.model.TodoPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TodoViewModel(
    private val getTodosUseCase: GetTodosUseCase,
    private val addTodoUseCase: AddTodoUseCase // Добавляем UseCase для добавления задачи
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
                val newTodo = Todo(
                    id = 0L,
                    content = content,
                    status = "ACTIVE",
                    priority = Priority.valueOf(priority)
                )
                addTodoUseCase(newTodo, userId)
                loadTodos(userId) // Обновляем список после добавления
            } catch (e: Exception) {
                //todo  Обрабатываем ошибку
            }
        }
    }
}