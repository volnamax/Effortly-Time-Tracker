package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Инициализация Koin
        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}