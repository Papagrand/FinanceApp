package ru.point.account.ui.mvi.accountEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.account.domain.usecase.GetAllAccountsUseCase
import ru.point.account.domain.usecase.UpdateAccountUseCase
import ru.point.api.flow.AccountPreferencesRepo
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
internal class AccountEditViewModelFactory @Inject constructor(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val prefs: AccountPreferencesRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        AccountEditViewModel(
            getAllAccountsUseCase = getAllAccountsUseCase,
            updateAccountUseCase = updateAccountUseCase,
            prefs = prefs,
        ) as T
}
