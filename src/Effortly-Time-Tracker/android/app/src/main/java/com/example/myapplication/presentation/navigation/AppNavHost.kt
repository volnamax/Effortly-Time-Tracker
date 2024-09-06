package com.example.myapplication.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.presentation.screen.auth.LoginScreen
import com.example.myapplication.presentation.screen.auth.RegisterScreen
import com.example.myapplication.presentation.screen.project.AddProjectScreen
import com.example.myapplication.presentation.screen.project.ProjectListScreen
import com.example.myapplication.presentation.screen.profile.ProfileScreen
import com.example.myapplication.presentation.screen.project.ProjectDetailScreen
import com.example.myapplication.presentation.screen.table.TableDetailScreen
import com.example.myapplication.presentation.screen.todo.TodoListScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.Todos.route) {
        composable(BottomNavItem.Todos.route) {
            TodoListScreen(navController)
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("projects") {
            ProjectListScreen(navController)
        }
        composable("addProject") {
            AddProjectScreen(navController)
        }
        composable("projectDetail/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull()
            projectId?.let {
                ProjectDetailScreen(projectId = it, navController = navController)
            }
        }
        composable("projectDetail/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull()
            projectId?.let {
                ProjectDetailScreen(projectId = it, navController = navController)
            }
        }
        // Добавление экрана для отображения деталей таблицы
        composable("tableDetail/{tableId}") { backStackEntry ->
            val tableId = backStackEntry.arguments?.getString("tableId")?.toIntOrNull()
            tableId?.let {
                TableDetailScreen(tableId = it, navController = navController)
            }
        }

    }
}