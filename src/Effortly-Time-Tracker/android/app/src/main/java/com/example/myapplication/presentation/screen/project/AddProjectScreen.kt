package com.example.myapplication.presentation.screen.project


import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.context.TokenManager
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddProjectScreen(navController: NavController, viewModel: ProjectViewModel = koinViewModel()) {
    val context = LocalContext.current
    var projectName by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(-1) }

    // Получаем userId из токена
    LaunchedEffect(Unit) {
        userId = TokenManager.getUserId(context) ?: -1
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Добавить новый проект")

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = projectName,
            onValueChange = { projectName = it },
            label = { Text(text = "Название проекта") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = projectDescription,
            onValueChange = { projectDescription = it },
            label = { Text(text = "Описание проекта (необязательно)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (projectName.isNotEmpty() && userId != -1) {
                    viewModel.addProject(projectName, projectDescription, userId)
                    navController.popBackStack()  // Возвращаемся на предыдущий экран после добавления
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Добавить проект")
        }
    }
}
