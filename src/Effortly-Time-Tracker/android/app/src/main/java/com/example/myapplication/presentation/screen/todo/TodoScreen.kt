package com.example.myapplication.presentation.screen.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.context.TokenManager
import com.example.myapplication.domain.model.Priority
import com.example.myapplication.presentation.model.TodoPresentation
import org.koin.androidx.compose.koinViewModel

@Composable
fun TodoListScreen(
    navController: NavController,
    viewModel: TodoViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val userId = remember { TokenManager.getUserId(context) ?: -1 }

    var newTodoContent by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf("IMPORTANT_URGENTLY") }

    LaunchedEffect(userId) {
        if (userId != -1) {
            viewModel.loadTodos(userId)
        }
    }

    val todos = viewModel.todoState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (todos.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No tasks available", color = Color.Gray)
            }
        } else {
            TodoCard(todoItems = todos.value)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Форма для добавления нового todo
            TextField(
                value = newTodoContent,
                onValueChange = { newTodoContent = it },
                label = { Text("Введите название дела") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Выбор приоритета
            DropdownMenu(
                expanded = true, // Сделайте выбор приоритета интерактивным
                onDismissRequest = { /* закрытие меню */ }
            ) {
                listOf(
                    "IMPORTANT_URGENTLY",
                    "NO_IMPORTANT_URGENTLY",
                    "IMPORTANT_NO_URGENTLY",
                    "NO_IMPORTANT_NO_URGENTLY"
                ).forEach { priority ->
                    DropdownMenuItem(onClick = { selectedPriority = priority }) {
                        Text(text = priority)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.addTodo(newTodoContent, selectedPriority, userId)
                    newTodoContent = "" // Очищаем поле ввода после добавления
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Todo")
            }
        }

    }
}


@Composable
fun TodoCard(todoItems: List<TodoPresentation>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp,
        backgroundColor = Color(0xFFE3F2FD) // Задаём цвет фона для карточки
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Дела",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Отображаем список задач
            LazyColumn {
                items(todoItems) { todo ->
                    TodoItem(todo)
                }
            }
        }
    }
}

@Composable
fun TodoItem(todo: TodoPresentation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.status == "DONE", // Измените в зависимости от состояния задачи
            onCheckedChange = { /* TODO: Добавить обработку завершения задачи */ }
        )
        Text(
            text = todo.content,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            fontWeight = if (todo.status == "DONE") FontWeight.Normal else FontWeight.Bold
        )

        // Иконка приоритета (например, восклицательный знак для важных задач)
        when (todo.priority) {
            Priority.IMPORTANT_URGENTLY -> {
                Text(
                    text = "!",
                    fontSize = 20.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Priority.NO_IMPORTANT_URGENTLY -> {
                Text(
                    text = "!",
                    fontSize = 20.sp,
                    color = Color.Yellow,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }

            else -> {
                // Для остальных приоритетов иконку можно не отображать
            }
        }
    }
}