package com.example.myapplication.datasource.remote.api


import android.content.Context
import com.example.myapplication.context.AuthInterceptor
import com.example.myapplication.data.datasourse.remote.sevice.AuthService
import com.example.myapplication.data.datasourse.remote.sevice.ProjectService
import com.example.myapplication.data.datasourse.remote.sevice.TableService
import com.example.myapplication.data.datasourse.remote.sevice.TodoService
import com.example.myapplication.data.datasourse.remote.sevice.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"


    // Создаём OkHttpClient с Interceptor
    private fun createOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))  // Добавляем AuthInterceptor
            .build()
    }

    // Создаём Retrofit клиент
    fun createRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(context))  // Используем кастомный OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createAuthService(context: Context): AuthService {
        return createRetrofit(context).create(AuthService::class.java)
    }

    fun createTodoService(context: Context): TodoService {
        return createRetrofit(context).create(TodoService::class.java)
    }


    fun createUserService(context: Context): UserService {
        return createRetrofit(context).create(UserService::class.java)
    }

    fun createProjectService(context: Context): ProjectService {
        return createRetrofit(context).create(ProjectService::class.java)
    }
    fun createTableService(context: Context): TableService {
        return createRetrofit(context).create(TableService::class.java)
    }
}