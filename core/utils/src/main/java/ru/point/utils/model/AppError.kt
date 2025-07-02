package ru.point.utils.model

import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

sealed class AppError {
    data class Http(val code: Int, val body: String?) : AppError()

    object NoInternet : AppError()

    object UnknownHost : AppError()

    object Unauthorized : AppError()

    object NotFound : AppError()

    object BadRequest : AppError()

    object ServerError : AppError()

    data class Unknown(val t: Throwable) : AppError()
}

fun Throwable.toAppError(): AppError =
    when (this) {
        is UnknownHostException, is NoInternetException ->
            AppError.NoInternet

        is HttpException -> {
            val body = response()?.errorBody()?.string().orEmpty()
            when (code()) {
                400 -> AppError.BadRequest
                401 -> AppError.Unauthorized
                404 -> AppError.NotFound
                in 500..599 -> AppError.ServerError
                else -> AppError.Http(code(), body)
            }
        }

        else -> AppError.Unknown(this)
    }

class NoInternetException : IOException("No internet")
