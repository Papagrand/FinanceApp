package ru.point.account.ui.mvi.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.account.domain.usecase.GetAllAccountsUseCase
import javax.inject.Inject
import ru.point.account.domain.usecase.GetChartsUseCase
import ru.point.api.flow.AccountPreferencesRepo

@Suppress("UNCHECKED_CAST")
internal class AccountViewModelFactory @Inject constructor(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val getChartsUseCase: GetChartsUseCase,
    private val prefs: AccountPreferencesRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) = AccountViewModel(
        getAllAccountsUseCase = getAllAccountsUseCase,
        getChartsUseCase = getChartsUseCase,
        prefs = prefs
    ) as T
}
