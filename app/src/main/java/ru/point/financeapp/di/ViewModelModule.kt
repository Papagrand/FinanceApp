package ru.point.financeapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.point.account.presentation.mvi.AccountViewModel
import ru.point.categories.presentation.mvi.CategoriesViewModel
import ru.point.expenses.presentation.mvi.ExpensesViewModel
import ru.point.financeapp.MainActivityViewModel
import ru.point.history.presentation.mvi.HistoryViewModel
import ru.point.income.presentation.mvi.incomes.IncomesViewModel

/**
 * ViewModelModule
 *
 * Подключает:
 *  - ViewModelFactory как ViewModelProvider.Factory
 *  - В каждый ключ Map<Class<ViewModel>, Provider<ViewModel>> пробрасывает конкретные ViewModel:
 *    AccountViewModel, CategoriesViewModel, HistoryViewModel,
 *    ExpensesViewModel, IncomesViewModel, MainActivityViewModel.
 */

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountVM(vm: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    abstract fun bindCategoriesVM(vm: CategoriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    abstract fun bindHistoryVM(vm: HistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class)
    abstract fun bindExpensesVM(vm: ExpensesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomesViewModel::class)
    abstract fun bindIncomesVM(vm: IncomesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityVM(vm: MainActivityViewModel): ViewModel
}
