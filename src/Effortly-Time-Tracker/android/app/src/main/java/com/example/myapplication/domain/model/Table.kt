package com.example.myapplication.domain.model


data class Table(
    val tableId: Int,          // Идентификатор таблицы
    val name: String,          // Название таблицы
    val description: String?,  // Описание таблицы
    val status: String,        // Статус таблицы ("ACTIVE", "NO_ACTIVE")
    val projectId: Int,        // Идентификатор проекта, которому принадлежит таблица
    val tasks: List<Int>       // Список идентификаторов задач, связанных с таблицей
)
