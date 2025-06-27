package ru.point.financeapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.point.account.presentation.mvi.AccountViewModel
import ru.point.categories.presentation.mvi.CategoriesViewModel
import ru.point.expenses.presentation.mvi.expenses.ExpensesViewModel
import ru.point.expenses.presentation.mvi.expensesHistory.ExpensesHistoryViewModel
import ru.point.financeapp.MainActivityViewModel
import ru.point.income.presentation.mvi.incomes.IncomesViewModel
import ru.point.income.presentation.mvi.incomesHistory.IncomesHistoryViewModel

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
    @ViewModelKey(ExpensesHistoryViewModel::class)
    abstract fun bindExpensesHistoryVM(vm: ExpensesHistoryViewModel): ViewModel

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
    @ViewModelKey(IncomesHistoryViewModel::class)
    abstract fun bindIncomesHistoryVM(vm: IncomesHistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityVM(vm: MainActivityViewModel): ViewModel
}
