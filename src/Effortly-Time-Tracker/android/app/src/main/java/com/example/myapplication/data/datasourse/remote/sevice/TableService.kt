package com.example.myapplication.data.datasourse.remote.sevice

import com.example.myapplication.domain.model.Table
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TableService {

    @GET("/api/table/get-all-by-project-id")
    suspend fun getTablesByProjectId(@Query("id") projectId: Int): List<Table>

    @POST("/api/table/add")
    suspend fun addTable(@Body tableDTO: Table): Table
}