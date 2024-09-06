package com.example.myapplication.domain.repository

import com.example.myapplication.datasource.remote.model.ProjectCreateDTO
import com.example.myapplication.domain.model.Project

interface ProjectRepository {
    suspend fun getProjectsByUserId(userId: Int): List<Project>
    suspend fun addProject(projectCreateDTO: ProjectCreateDTO): ProjectCreateDTO

}