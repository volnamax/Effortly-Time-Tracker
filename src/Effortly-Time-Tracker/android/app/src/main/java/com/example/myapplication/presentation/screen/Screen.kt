package com.example.myapplication.presentation.screen

import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.LoginRequestDto
import com.example.myapplication.RegisterRequestDto

@Composable
fun LoginScreen(viewModel: AuthViewModel) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.login(LoginRequestDto(login, password))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        loginState?.let {
            Text("Welcome, ${it.user.email}", style = MaterialTheme.typography.h6)
        }
    }
}
@Composable
fun RegisterScreen(viewModel: AuthViewModel) {
    var userName by remember { mutableStateOf("") }
    var userSecondname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    val registerState by viewModel.registerState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = userSecondname,
            onValueChange = { userSecondname = it },
            label = { Text("Surname") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = role,
            onValueChange = { role = it },
            label = { Text("Role (ADMIN, MANAGER, USER)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.register(
                    RegisterRequestDto(
                        userName = userName,
                        userSecondname = userSecondname,
                        email = email,
                        passwordHash = password,
                        role = role
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        registerState?.let {
            Text("Welcome, ${it.user.email}", style = MaterialTheme.typography.h6)
        }
    }
}
