package com.example.myapplication.data.repository

import com.example.myapplication.data.datasourse.remote.sevice.ProjectService
import com.example.myapplication.datasource.remote.model.ProjectCreateDTO
import com.example.myapplication.domain.model.Project
import com.example.myapplication.domain.repository.ProjectRepository

class ProjectRepositoryImpl(private val projectService: ProjectService) : ProjectRepository {
    override suspend fun getProjectsByUserId(userId: Int): List<Project> {
        return projectService.getProjectsByUserId(userId)
    }
    override suspend fun addProject(projectCreateDTO: ProjectCreateDTO): ProjectCreateDTO {
        return projectService.addProject(projectCreateDTO)
    }
}