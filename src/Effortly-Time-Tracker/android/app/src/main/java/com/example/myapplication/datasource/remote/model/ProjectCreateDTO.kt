package com.example.myapplication.datasource.remote.model

data class ProjectCreateDTO(
    val name: String,
    val description: String?,
    val userProject: Int
)