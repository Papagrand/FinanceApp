package ru.point.utils.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object RetrofitProvider {
    val json = Json { ignoreUnknownKeys = true }
    val contentType = "application/json".toMediaType()

    private lateinit var _instance: Retrofit
    val instance get() = _instance

    fun init(tracker: NetworkTracker) {
        val okHttp =
            OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor())
                .addInterceptor(RetryInterceptor(tracker))
                .build()

        _instance =
            Retrofit.Builder()
                .baseUrl("https://shmr-finance.ru/api/v1/")
                .addConverterFactory(json.asConverterFactory(contentType))
                .client(okHttp)
                .build()
    }
}
