package com.example.myapplication.data.datasourse.remote.sevice

import com.example.myapplication.datasource.remote.model.StatusEnum
import com.example.myapplication.datasource.remote.model.TodoNodeDTO
import com.example.myapplication.datasource.remote.model.TodoNodeResponseDTO
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface TodoService {
    @GET("/api/todo/get-all-by-user-id")
    suspend fun getTodosByUserId(@Query("id") userId: Int): List<TodoNodeResponseDTO>

    @POST("/api/todo/add")
    suspend fun addTodo(
        @Body todoNodeDTO: TodoNodeDTO
    ): TodoNodeDTO

    @PATCH("/api/todo/update-status")
    suspend fun updateTodoStatus(
        @Query("id") id: Long,
        @Query("status") status: StatusEnum
    ): TodoNodeResponseDTO
}