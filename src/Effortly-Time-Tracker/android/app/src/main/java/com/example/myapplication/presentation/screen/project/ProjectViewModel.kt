package com.example.myapplication.presentation.screen.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.datasource.remote.model.ProjectCreateDTO
import com.example.myapplication.domain.model.Project
import com.example.myapplication.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProjectViewModel(private val projectRepository: ProjectRepository) : ViewModel() {

    private val _projectState = MutableStateFlow<List<Project>>(emptyList())
    val projectState: StateFlow<List<Project>> = _projectState

    // Загружаем все проекты для пользователя
    fun loadProjects(userId: Int) {
        viewModelScope.launch {
            try {
                val projects = projectRepository.getProjectsByUserId(userId)
                _projectState.value = projects
            } catch (e: Exception) {
                // Обрабатываем ошибки
            }
        }
    }

    fun addProject(name: String, description: String, userId: Int) {
        viewModelScope.launch {
            try {
                val projectCreateDTO = ProjectCreateDTO(name = name, description = description, userProject = userId)
                projectRepository.addProject(projectCreateDTO)
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }

}