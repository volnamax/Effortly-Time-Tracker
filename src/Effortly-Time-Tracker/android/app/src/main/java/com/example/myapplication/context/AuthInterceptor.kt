package com.example.myapplication.context
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // Получаем токен из SharedPreferences
        val token = TokenManager.getToken(context)

        // Добавляем токен в заголовок, если он существует
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        // Продолжаем запрос с новым заголовком
        return chain.proceed(requestBuilder.build())
    }
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val requestBuilder = chain.request().newBuilder()
//
//        val token = TokenManager.getToken(context)
//
//        token?.let {
//            requestBuilder.addHeader("Authorization", "Bearer $it")
//        }
//
//        val response = chain.proceed(requestBuilder.build())
//
//        // Проверяем, истек ли токен (например, код 401)
//        if (response.code == 401) {
//            // Здесь можно реализовать логику для обновления токена или выхода пользователя из системы
//            Log.e("AuthInterceptor", "Unauthorized! Token expired or invalid.")
//            // Можно перенаправить на страницу логина или запросить обновление токена
////        }
//
//        return response
//    }
}
