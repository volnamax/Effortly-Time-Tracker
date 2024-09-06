package com.example.myapplication.presentation.mapper


import com.example.myapplication.domain.model.Todo
import com.example.myapplication.presentation.model.TodoPresentation

// Преобразование Todвo (из domain) в TodoPresentation (для UI)
fun Todo.toPresentation(): TodoPresentation {
    return TodoPresentation(
        id = this.id,
        content = this.content,
        status = this.status,
        priority = this.priority
    )
}