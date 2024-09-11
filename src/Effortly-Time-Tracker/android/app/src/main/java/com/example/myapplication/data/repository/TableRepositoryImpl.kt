package com.example.myapplication.data.repository


import com.example.myapplication.data.datasourse.remote.sevice.TableService

import com.example.myapplication.domain.model.Table
import com.example.myapplication.domain.repository.TableRepository

class TableRepositoryImpl(
    private val tableService: TableService // Сервис для взаимодействия с API
) : TableRepository {

    // Получаем все таблицы по идентификатору проекта
    override suspend fun getTablesByProjectId(projectId: Int): List<Table> {
        val tableResponseDTOs = tableService.getTablesByProjectId(projectId)
        return tableResponseDTOs // Преобразуем DTO в доменную модель
    }

    // Добавляем новую таблицу
    override suspend fun addTable(table: Table) {
        tableService.addTable(table)
    }
}