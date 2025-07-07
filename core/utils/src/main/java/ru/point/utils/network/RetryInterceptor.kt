package ru.point.utils.network

import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var tries = 0
        var response = chain.proceed(chain.request())

        while (response.code == 500 && tries < MAX_RETRIES) {
            tries++
            response.close()
            Thread.sleep(DELAY_MS)
            response = chain.proceed(chain.request())
        }

        return response
    }

    companion object {
        private const val MAX_RETRIES: Int = 3
        private const val DELAY_MS: Long = 2_000
    }
}
