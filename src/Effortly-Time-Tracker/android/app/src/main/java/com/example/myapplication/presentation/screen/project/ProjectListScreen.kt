package com.example.myapplication.presentation.screen.project


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.myapplication.domain.model.Project
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProjectListScreen(
    navController: NavController,
    viewModel: ProjectViewModel = koinViewModel()
) {
    // Получаем контекст и userId из токена
    val context = LocalContext.current
    var userId by remember { mutableStateOf(-1) }

    // Загружаем userId из токена
    LaunchedEffect(Unit) {
        val token = TokenManager.getToken(context)
        token?.let {
            TokenManager.getUserIdFromToken(it, context) { id ->
                userId = id ?: -1
            }
        }
    }

    // Загружаем проекты после получения userId
    LaunchedEffect(userId) {
        if (userId != -1) {
            viewModel.loadProjects(userId)
        }
    }

    // Наблюдаем за состоянием проектов
    val projects by viewModel.projectState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Проекты", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Blue)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(projects) { project ->
                ProjectItem(project = project, onClick = {
                    // Переход на экран проекта
                    navController.navigate("projectDetail/${project.id}")
                })
            }
        }

        // Кнопка добавления нового проекта
        FloatingActionButton(
            onClick = {
                navController.navigate("addProject")
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Project")
        }
    }
}

@Composable
fun ProjectItem(project: Project, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(text = project.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = project.description ?: "Без описания", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

