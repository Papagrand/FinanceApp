package ru.point.network.client

import okhttp3.Interceptor
import okhttp3.Response
import ru.point.network.BuildConfig
import java.util.Properties

class ApiKeyInterceptor: Interceptor {
    private val token = BuildConfig.API_KEY

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
            .newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}
