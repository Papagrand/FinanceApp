package ru.point.utils.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(
    private val maxRetries: Int = 3,
    private val delayMs: Long = 2_000,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var tryCount = 0
        while (true) {
            try {
                return chain.proceed(chain.request())
            } catch (ioe: IOException) {
                if (++tryCount > maxRetries) throw ioe
                Thread.sleep(delayMs)
            }
        }
    }
}
