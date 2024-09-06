package com.example.myapplication.presentation.screen.table
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.context.TokenManager
import com.example.myapplication.domain.model.Task
import org.koin.androidx.compose.koinViewModel

@Composable
fun TableDetailScreen(
    tableId: Int,
    navController: NavController,
    viewModel: TableDetailViewModel = koinViewModel()
) {
    // Получаем userId из токена
    val context = LocalContext.current
    var userId by remember { mutableStateOf(-1) }
    var newTaskName by remember { mutableStateOf("") }
    var newTaskDescription by remember { mutableStateOf("") }

    // Загружаем userId из токена
    LaunchedEffect(Unit) {
        val token = TokenManager.getToken(context)
        token?.let {
            TokenManager.getUserIdFromToken(it, context) { id ->
                userId = id ?: -1
            }
        }
    }

    // Загружаем задачи таблицы после получения userId
    LaunchedEffect(userId) {
        if (userId != -1) {
            viewModel.loadTasksByTableId(tableId)
        }
    }

    // Наблюдаем за состоянием задач
    val tasks by viewModel.tasksState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Таблица", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Blue)
        Spacer(modifier = Modifier.height(8.dp))

        // Поля для добавления новой задачи
        TextField(
            value = newTaskName,
            onValueChange = { newTaskName = it },
            label = { Text("Название задачи") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = newTaskDescription,
            onValueChange = { newTaskDescription = it },
            label = { Text("Описание задачи") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка добавления задачи
        Button(
            onClick = {
                if (newTaskName.isNotEmpty()) {
                    // Добавляем задачу
                    viewModel.addTaskToTable(
                        tableId = tableId,
                        taskName = newTaskName,
                        taskDescription = newTaskDescription
                    )
                    // Очищаем поля ввода
                    newTaskName = ""
                    newTaskDescription = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить задачу")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Отображаем задачи таблицы
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tasks) { task ->
                TaskItem(task = task)
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = task.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = task.description ?: "Без описания", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
    }
}
