package ru.point.account.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.account.domain.usecase.GetAllAccountsUseCase

/**
 * AccountViewModelFactory
 *
 * Ответственность:
 * - создание экземпляров AccountViewModel с передачей юзкейсов;
 * - выбрасывание исключения при неподдерживаемом классе ViewModel.
 *
 */

class AccountViewModelFactory(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(getAllAccountsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
