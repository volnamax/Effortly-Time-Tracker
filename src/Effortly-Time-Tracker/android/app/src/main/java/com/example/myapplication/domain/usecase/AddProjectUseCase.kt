package com.example.myapplication.domain.usecase

import com.example.myapplication.datasource.remote.model.ProjectCreateDTO
import com.example.myapplication.domain.repository.ProjectRepository


class AddProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectCreateDTO: ProjectCreateDTO) {
        projectRepository.addProject(projectCreateDTO)
    }
}