package com.example.myapplication.di


import TodoViewModel
import com.example.myapplication.data.repository.AuthRepositoryImpl
import com.example.myapplication.data.repository.TodoRepositoryImpl
import com.example.myapplication.datasource.remote.api.RetrofitClient
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.TodoRepository
import com.example.myapplication.domain.usecase.todo.GetTodosUseCase
import com.example.myapplication.domain.usecase.LoginUseCase
import com.example.myapplication.domain.usecase.RegisterUseCase
import com.example.myapplication.domain.usecase.todo.AddTodoUseCase
import com.example.myapplication.domain.usecase.todo.UpdateTodoStatusUseCase
import com.example.myapplication.presentation.screen.auth.AuthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {


    single<AuthRepository> { AuthRepositoryImpl(RetrofitClient.createAuthService(androidContext())) }
    single<TodoRepository> { TodoRepositoryImpl(RetrofitClient.createTodoService(androidContext())) }

    single { RegisterUseCase(get()) }
    single { LoginUseCase(get()) }
    single { GetTodosUseCase(get()) }

    viewModel { AuthViewModel(get(), get()) }

    single { AddTodoUseCase(get()) }
    viewModel { TodoViewModel(get(), get(), get()) } // Передаём оба use case в viewModel

    factory { UpdateTodoStatusUseCase(get()) }

}