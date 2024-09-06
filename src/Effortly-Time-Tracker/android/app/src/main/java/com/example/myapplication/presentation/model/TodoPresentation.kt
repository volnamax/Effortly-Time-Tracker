package com.example.myapplication.presentation.model

import com.example.myapplication.datasource.remote.model.StatusEnum
import com.example.myapplication.domain.model.Priority

data class TodoPresentation(
    val id_todo: Long,
    val content: String,
    val status: StatusEnum,
    val priority: Priority
)