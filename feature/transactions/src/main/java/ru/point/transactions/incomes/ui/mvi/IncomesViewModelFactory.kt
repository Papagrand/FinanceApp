package ru.point.transactions.incomes.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.transactions.incomes.domain.usecase.GetIncomesTodayUseCase
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
internal class IncomesViewModelFactory @Inject constructor(
    private val getIncomesTodayUseCase: GetIncomesTodayUseCase,
    private val prefs: AccountPreferencesRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        IncomesViewModel(getIncomesTodayUseCase = getIncomesTodayUseCase, prefs) as T
}
