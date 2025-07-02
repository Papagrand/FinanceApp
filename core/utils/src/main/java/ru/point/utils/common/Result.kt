package ru.point.utils.common

import ru.point.utils.model.AppError

sealed class Result<out T> {
    object Loading : Result<Nothing>()

    data class Success<T>(val data: T) : Result<T>()

    data class Error(val cause: AppError) : Result<Nothing>()
}
