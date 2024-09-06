package com.example.myapplication.presentation.mapper


import com.example.myapplication.datasource.remote.model.TodoNodeResponseDTO
import com.example.myapplication.domain.model.Todo
import com.example.myapplication.presentation.model.TodoPresentation

// Преобразование Todo (из domain) в TodoPresentation (для UI)
fun Todo.toPresentation(): TodoPresentation {
    return TodoPresentation(
        id_todo = this.id_todo,
        content = this.content,
        status = this.status,
        priority = this.priority
    )
}


fun Todo.toDomain(): Todo {
    return Todo(
        id_todo = this.id_todo,
        content = this.content,
        status = this.status,
        priority = this.priority
    )
}