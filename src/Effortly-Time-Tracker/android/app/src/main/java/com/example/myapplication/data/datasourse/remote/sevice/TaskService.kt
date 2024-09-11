package com.example.myapplication.data.datasourse.remote.sevice

import com.example.myapplication.domain.model.Task
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TaskService {
    @POST("/api/task/add")
    suspend fun addTask( @Body task: Task): Task

    @GET("api/task/get-all-by-table-id")
    suspend fun getTaskByTableId(  @Query("id") id: Int): List<Task>
}