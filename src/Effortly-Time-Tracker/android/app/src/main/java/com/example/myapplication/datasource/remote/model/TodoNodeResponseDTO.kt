package com.example.myapplication.datasource.remote.model

data class TodoNodeResponseDTO(
    val id: Long,
    val content: String,
    val status: String,
    val priority: String
)