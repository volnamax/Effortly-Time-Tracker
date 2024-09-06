package com.example.myapplication.di


import com.example.myapplication.data.repository.AuthRepositoryImpl
import com.example.myapplication.datasource.remote.api.RetrofitClient
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.usecase.LoginUseCase
import com.example.myapplication.domain.usecase.RegisterUseCase
import com.example.myapplication.presentation.screen.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetrofitClient.createAuthService(get()) }  // Передаем context через Koin

    single<AuthRepository> { AuthRepositoryImpl(get()) }

    single { RegisterUseCase(get()) }

    single { LoginUseCase(get()) }

    viewModel { AuthViewModel(get(), get()) }
}