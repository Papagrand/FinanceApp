package ru.point.account.ui.mvi.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.account.domain.usecase.GetAllAccountsUseCase
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
internal class AccountViewModelFactory @Inject constructor(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) = AccountViewModel(getAllAccountsUseCase = getAllAccountsUseCase) as T
}
