package com.example.myapplication.presentation.screen.table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Task
import com.example.myapplication.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TableDetailViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _tasksState = MutableStateFlow<List<Task>>(emptyList())
    val tasksState: StateFlow<List<Task>> = _tasksState

    // Функция для загрузки задач по id таблицы
    fun loadTasksByTableId(tableId: Int) {
        viewModelScope.launch {
            try {
                val tasks = taskRepository.getTasksByTableId(tableId)
                _tasksState.value = tasks
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }

    // Функция для добавления новой задачи в таблицу
    fun addTaskToTable(tableId: Int, taskName: String, taskDescription: String) {
        viewModelScope.launch {
            try {
                val newTask = Task(
                    taskId = 0,
                    name = taskName,
                    description = taskDescription,
                    status = "ACTIVE",
                    tableId = tableId
                )
                taskRepository.addTask(newTask)
                loadTasksByTableId(tableId) // Перезагружаем задачи после добавления
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }
}
