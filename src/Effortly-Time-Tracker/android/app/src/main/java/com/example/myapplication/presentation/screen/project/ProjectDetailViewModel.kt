package com.example.myapplication.presentation.screen.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Table
import com.example.myapplication.domain.usecase.table.GetTablesByProjectIdUseCase
import com.example.myapplication.domain.usecase.table.AddTableUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProjectDetailViewModel(
    private val getTablesByProjectIdUseCase: GetTablesByProjectIdUseCase,
    private val addTableUseCase: AddTableUseCase
) : ViewModel() {

    private val _tableState = MutableStateFlow<List<Table>>(emptyList())
    val tableState: StateFlow<List<Table>> = _tableState



    // Метод для загрузки таблиц по id проекта
    fun loadTablesByProjectId(projectId: Int) {
        viewModelScope.launch {
            try {
                val tables = getTablesByProjectIdUseCase(projectId)
                _tableState.value = tables
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }
    fun addTable(projectId: Int, tableName: String, tableDescription: String) {
        viewModelScope.launch {
            try {
                val newTable = Table(
                    tableId = 0, // Новый объект, сервер присвоит правильный ID
                    name = tableName,
                    description = tableDescription,
                    status = "ACTIVE", // По умолчанию статус активен
                    projectId = projectId,
                    tasks = emptyList() // Пока без задач
                )
                addTableUseCase(newTable)
                loadTablesByProjectId(projectId) // Перезагружаем таблицы после добавления новой
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

}
