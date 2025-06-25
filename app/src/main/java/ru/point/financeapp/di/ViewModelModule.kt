package ru.point.financeapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.point.account.presentation.mvi.AccountViewModel
import ru.point.financeapp.MainActivityViewModel

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountVM(vm: AccountViewModel): ViewModel

    @Binds @IntoMap @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityVM(vm: MainActivityViewModel): ViewModel
}