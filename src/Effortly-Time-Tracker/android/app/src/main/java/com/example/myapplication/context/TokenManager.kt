package com.example.myapplication.context

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.example.myapplication.datasource.remote.api.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object TokenManager {
    private const val PREFS_NAME = "my_app_prefs"
    private const val TOKEN_KEY = "jwt_token"
    private const val USER_ID_KEY = "user_id"

    // Сохранение токена и userId
    fun saveToken(context: Context, token: String, userId: Int) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(TOKEN_KEY, token)
            .putInt(USER_ID_KEY, userId)
            .apply()

        Log.d("TokenManager", "Token and UserID saved: $token, User ID - $userId")
    }

    // Получение токена
    fun getToken(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(TOKEN_KEY, null)
        Log.d("TokenManager", "Retrieved token: $token")
        return token
    }

    // Получение UserID
    fun getUserId(context: Context): Int? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt(USER_ID_KEY, -1)
        return if (userId != -1) userId else null
    }

    // Очистка токена и userId
    fun clearToken(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(TOKEN_KEY).remove(USER_ID_KEY).apply()
        Log.d("TokenManager", "Token and UserID cleared")
    }
    // New method to extract the email from the token
    fun getEmailFromToken(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size == 3) {
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
                val jsonObject = JSONObject(payload)
                jsonObject.getString("sub") // Assuming "sub" contains the email
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("TokenManager", "Error extracting email from token: ${e.message}")
            null
        }
    }
    // Метод для извлечения userId из JWT токена по email
    fun getUserIdFromToken(token: String, context: Context, onSuccess: (Int?) -> Unit) {
        try {
            // Разбиваем JWT на три части: header, payload и signature
            val parts = token.split(".")
            if (parts.size == 3) {
                // Декодируем payload, который хранит данные о пользователе
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
                val jsonObject = JSONObject(payload)
                val userEmail = jsonObject.getString("sub")

                // Вызываем API для получения userId по email
                getUserIdByEmail(userEmail, context, onSuccess)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Запрос к серверу для получения userId по email
    private fun getUserIdByEmail(email: String, context: Context, onSuccess: (Int?) -> Unit) {
        val authService = RetrofitClient.createAuthService(context)
        val call = authService.getUserIdByEmail(email)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val userId = response.body()
                    Log.d("TokenManager", "User ID retrieved: $userId for email: $email")
                    onSuccess(userId)
                } else {
                    Log.e("TokenManager", "Failed to retrieve User ID for email: $email")
                    onSuccess(null)
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                t.printStackTrace()
                onSuccess(null)
            }
        })
    }
}
