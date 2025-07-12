package ru.point.transactions.addOrEditTransaction.ui.mvi

import ru.point.api.model.AllCategoriesDto
import ru.point.api.model.CategoryDto
import ru.point.utils.extensionsAndParsers.ScreenEnums

internal data class AddOrEditTransactionState(
    val screenMode: ScreenEnums = ScreenEnums.CREATE,
    val isIncome: Boolean = false,
    val transactionId: Int? = null,
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val error: String? = null,
    val noInternet: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val accountName: String = "",
    val pickedCategory: CategoryDto? = null,
    val allCategories: List<AllCategoriesDto> = emptyList(),
    val amountInput: String = "",
    val amountValid: Boolean = true,
    val amountError: String? = null,
    val date: String = "",
    val time: String = "",
    val datetime: String = "",
    val comment: String = "",
)
