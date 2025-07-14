package ru.point.impl.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.point.utils.common.Result
import ru.point.utils.model.toAppError

internal fun <T> safeApiFlow(apiCall: suspend () -> T): Flow<Result<T>> =
    flow {
        emit(Result.Loading)
        try {
            emit(Result.Success(apiCall()))
        } catch (e: Throwable) {
            emit(Result.Error(e.toAppError()))
        }
    }.flowOn(Dispatchers.IO)
