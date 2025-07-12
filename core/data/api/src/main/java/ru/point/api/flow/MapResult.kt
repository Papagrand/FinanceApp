package ru.point.api.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.model.AllCategoriesDto
import ru.point.api.model.CategoryDto
import ru.point.api.model.TodayTransactions
import ru.point.api.model.TransactionDto
import ru.point.utils.common.Result
import java.time.Instant
import java.time.ZoneId

fun <T, R> Flow<Result<T>>.mapResult(transform: suspend (T) -> R): Flow<Result<R>> =
    this.map { result ->
        when (result) {
            is Result.Loading -> Result.Loading
            is Result.Error -> Result.Error(result.cause)
            is Result.Success -> Result.Success(transform(result.data))
        }
    }

fun Flow<Result<List<TransactionDto>>>.toTodayTransactions(isIncome: Boolean): Flow<Result<TodayTransactions>> =
    this
        .mapResult { list ->
            val filtered =
                list.filter { it.isIncome == isIncome }
                    .sortedByDescending { dto ->
                        Instant.parse(dto.dateTime)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
                    }
            TodayTransactions(
                transactionsList = filtered,
                total = filtered.sumOf { it.amount.toBigDecimal() },
            )
        }

fun AllCategoriesDto.toCategoryDto(): CategoryDto =
    CategoryDto(
        categoryId = categoryId,
        categoryName = categoryName,
        emoji = emoji,
        amount = "",
    )
