package com.example.myapplication.datasource.remote.model

data class TodoNodeResponseDTO(
    val id_todo: Long,
    val content: String,
    val status: String,
    val priority: String
)

data class TodoNodeDTO(
    val content: String,
    val status: String = "ACTIVE",  // По умолчанию задача активна
    val priority: String,
    val userID: Int
)