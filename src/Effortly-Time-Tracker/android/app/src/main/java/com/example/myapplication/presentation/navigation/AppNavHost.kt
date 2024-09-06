package com.example.myapplication.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.presentation.screen.auth.LoginScreen
import com.example.myapplication.presentation.screen.auth.RegisterScreen
import com.example.myapplication.presentation.screen.todo.TodoListScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("todo") {
            TodoListScreen(navController)
        }
    }
}
