package com.example.myapplication.presentation.screen.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = koinViewModel()) {

    // Retrieve the context and token
    val context = LocalContext.current
    var email by remember { mutableStateOf<String?>(null) }

    // Automatically load the email from the token when the screen is first launched
    LaunchedEffect(Unit) {
        val token = TokenManager.getToken(context)
        token?.let {
            email = TokenManager.getEmailFromToken(it) // Extract email directly from the token
        }
    }

    // Fetch user data once the email is loaded

    LaunchedEffect(email) {
        email?.let {
            Log.e("APPTAG", "APPTAG $email")
            viewModel.getUserByEmail(it)
        }
    }

    // Observe user data from the ViewModel
    val user by viewModel.userState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Профиль", fontSize = 30.sp, color = Color.Blue, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        user?.let {
            ProfileField("Имя", it.userName)
            ProfileField("Фамилия", it.userSecondname)
            ProfileField("Роль", it.role)
            ProfileField("Почта", it.email)

            Spacer(modifier = Modifier.height(24.dp))


        } ?: run {
            // Loading or error handling
            Text(text = "Загрузка профиля...")
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.Blue, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Normal)
    }
}
