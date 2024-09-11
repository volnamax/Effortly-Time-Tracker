package com.example.myapplication.domain.model

data class Project(
    val id: Int,
    val name: String,
    val description: String?,
    val userProject: Int,
    val tablesId: List<Int>,
    val tagsId: Set<Int>,
    val groupId: Int?
)