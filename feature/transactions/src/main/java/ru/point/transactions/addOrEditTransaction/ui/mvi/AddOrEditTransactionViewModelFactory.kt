package ru.point.transactions.addOrEditTransaction.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.transactions.addOrEditTransaction.domain.usecase.AddTransactionUseCase
import ru.point.transactions.addOrEditTransaction.domain.usecase.DeleteTransactionUseCase
import ru.point.transactions.addOrEditTransaction.domain.usecase.GetAllCategoriesUseCase
import ru.point.transactions.addOrEditTransaction.domain.usecase.GetTransactionInfoUseCase
import ru.point.transactions.addOrEditTransaction.domain.usecase.UpdateTransactionUseCase
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
internal class AddOrEditTransactionViewModelFactory @Inject constructor(
    private val transactionId: Int?,
    private val isIncome: Boolean,
    private val getTransactionInfoUseCase: GetTransactionInfoUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val prefs: AccountPreferencesRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        AddOrEditTransactionViewModel(
            transactionId = transactionId,
            isIncome = isIncome,
            getTransactionInfoUseCase,
            addTransactionUseCase,
            updateTransactionUseCase,
            deleteTransactionUseCase,
            getAllCategoriesUseCase,
            prefs,
        ) as T
}
