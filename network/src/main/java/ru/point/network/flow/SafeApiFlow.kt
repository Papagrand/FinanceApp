package ru.point.network.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.point.core.common.Result
import ru.point.core.error.toAppError

fun <T> safeApiFlow(apiCall: suspend () -> T): Flow<Result<T>> =
    flow {
        emit(Result.Loading)
        try {
            emit(Result.Success(apiCall()))
        } catch (e: Throwable) {
            emit(Result.Error(e.toAppError()))
        }
    }.flowOn(Dispatchers.IO)
