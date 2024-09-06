package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Task

interface TaskRepository {
    suspend fun getTasksByTableId(tableId: Int): List<Task>
    suspend fun addTask(task: Task)
}
