package ru.point.transactions.addOrEditTransaction.ui.mvi

import ru.point.api.model.CategoryDto

sealed interface AddOrEditTransactionIntent {
    data object Load : AddOrEditTransactionIntent

    data object Retry : AddOrEditTransactionIntent

    data object ClearError : AddOrEditTransactionIntent

    data object Save : AddOrEditTransactionIntent

    data object Cancel : AddOrEditTransactionIntent

    data object DeleteTransaction : AddOrEditTransactionIntent

    data class AmountChanged(val newAmount: String) : AddOrEditTransactionIntent

    data class DateChanged(val date: String) : AddOrEditTransactionIntent

    data class TimeChanged(val time: String) : AddOrEditTransactionIntent

    data class CommentChanged(val comment: String) : AddOrEditTransactionIntent

    data class CategoryPicked(val category: CategoryDto) : AddOrEditTransactionIntent
}
