package com.example.myapplication.presentation.screen.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
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
import com.example.myapplication.domain.model.Table
import org.koin.androidx.compose.koinViewModel
@Composable
fun ProjectDetailScreen(
    projectId: Int,
    navController: NavController,
    viewModel: ProjectDetailViewModel = koinViewModel()
) {
    // Получаем userId из токена
    val context = LocalContext.current
    var userId by remember { mutableStateOf(-1) }
    var newTableName by remember { mutableStateOf("") }
    var newTableDescription by remember { mutableStateOf("") }

    // Загружаем userId из токена
    LaunchedEffect(Unit) {
        val token = TokenManager.getToken(context)
        token?.let {
            TokenManager.getUserIdFromToken(it, context) { id ->
                userId = id ?: -1
            }
        }
    }

    // Загружаем таблицы проекта после получения userId
    LaunchedEffect(userId) {
        if (userId != -1) {
            viewModel.loadTablesByProjectId(projectId)
        }
    }

    // Наблюдаем за состоянием таблиц
    val tables by viewModel.tableState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Проект", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Blue)
        Spacer(modifier = Modifier.height(8.dp))

        // Поля для добавления новой таблицы
        TextField(
            value = newTableName,
            onValueChange = { newTableName = it },
            label = { Text("Название таблицы") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = newTableDescription,
            onValueChange = { newTableDescription = it },
            label = { Text("Описание таблицы") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка добавления таблицы
        Button(
            onClick = {
                if (newTableName.isNotEmpty()) {
                    // Добавляем таблицу
                    viewModel.addTable(
                        projectId = projectId,
                        tableName = newTableName,
                        tableDescription = newTableDescription
                    )
                    // Очищаем поля ввода
                    newTableName = ""
                    newTableDescription = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить таблицу")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Отображаем таблицы проекта
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tables) { table ->
                TableItem(table = table)
            }
        }
    }
}

@Composable
fun TableItem(table: Table) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = table.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = table.description ?: "Без описания", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
    }
}
