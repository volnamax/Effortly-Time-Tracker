package com.example.myapplication.presentation.model

import com.example.myapplication.domain.model.Priority

data class TodoPresentation(
    val id: Long,
    val content: String,
    val status: String,
    val priority: Priority
)