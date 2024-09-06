package com.example.myapplication.data.datasourse.remote.sevice

import com.example.myapplication.datasource.remote.model.ProjectCreateDTO
import com.example.myapplication.domain.model.Project
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ProjectService {
    @GET("/api/project/get-all-by-user-id")
    suspend fun getProjectsByUserId(@Query("id") userId: Int): List<Project>

    @POST("/api/project/add")
    suspend fun addProject(@Body projectCreateDTO: ProjectCreateDTO): ProjectCreateDTO
}
