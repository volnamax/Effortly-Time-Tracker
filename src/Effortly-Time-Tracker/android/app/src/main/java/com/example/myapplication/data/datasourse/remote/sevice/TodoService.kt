package com.example.myapplication.data.datasourse.remote.sevice

import com.example.myapplication.datasource.remote.model.TodoNodeResponseDTO
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TodoService {
    @GET("/api/todo/get-all-by-user-id")
    suspend fun getTodosByUserId(@Query("id") userId: Int): List<TodoNodeResponseDTO>

    @POST("/api/todo/add")
    suspend fun addTodo(
        @Query("content") content: String,
        @Query("priority") priority: String,
        @Query("user") userId: Int
    )
}