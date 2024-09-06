package com.example.myapplication.data.repository

import com.example.myapplication.data.datasourse.remote.sevice.TaskService
import com.example.myapplication.domain.model.Task
import com.example.myapplication.domain.repository.TaskRepository

class TaskRepositoryImpl(private val taskService: TaskService) : TaskRepository {
    override suspend fun getTasksByTableId(tableId: Int): List<Task> {
        return taskService.getTaskByTableId(tableId)
    }

    override suspend fun addTask(task: Task) {
        taskService.addTask(task)
    }
}