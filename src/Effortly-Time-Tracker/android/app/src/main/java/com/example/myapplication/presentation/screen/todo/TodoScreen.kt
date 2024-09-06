package com.example.myapplication.presentation.screen.todo

import TodoViewModel
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
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
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
import com.example.myapplication.datasource.remote.model.StatusEnum
import com.example.myapplication.domain.model.Priority
import com.example.myapplication.domain.model.Todo
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    navController: NavController,
    viewModel: TodoViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val userId = remember { TokenManager.getUserId(context) ?: -1 }

    var newTodoContent by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf("IMPORTANT_URGENTLY") }
    val priorityOptions = listOf(
        "IMPORTANT_URGENTLY",
        "NO_IMPORTANT_URGENTLY",
        "IMPORTANT_NO_URGENTLY",
        "NO_IMPORTANT_NO_URGENTLY"
    )

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
        // Form to add a new todo
        TextField(
            value = newTodoContent,
            onValueChange = { newTodoContent = it },
            label = { Text("Enter todo title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Priority dropdown menu
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                label = { Text(text = "Select Priority") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = selectedPriority,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                priorityOptions.forEach { priority ->
                    DropdownMenuItem(
                        onClick = {
                            selectedPriority = priority
                            expanded = false
                        }
                    ) {
                        Text(text = priority)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.addTodo(newTodoContent, selectedPriority, userId)
                newTodoContent = "" // Clear input after adding
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Todo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show only active tasks
        val activeTodos = todos.value.filter { it.status == StatusEnum.ACTIVE }

        if (activeTodos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No active tasks available", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(activeTodos) { todo ->
                    TodoItem(todo) { todoId, newStatus ->
                        viewModel.updateTodoStatus(todoId, newStatus, userId)
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItem(todo: Todo, onStatusChange: (Long, StatusEnum) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.status == StatusEnum.NO_ACTIVE,
            onCheckedChange = {
                val newStatus = if (todo.status == StatusEnum.NO_ACTIVE) StatusEnum.ACTIVE else StatusEnum.NO_ACTIVE
                onStatusChange(todo.todoId, newStatus) // Pass id and new status
            }
        )
        Text(
            text = todo.content,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            fontWeight = if (todo.status == StatusEnum.NO_ACTIVE) FontWeight.Normal else FontWeight.Bold
        )

        // Priority icons
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
            else -> {}
        }
    }
}


