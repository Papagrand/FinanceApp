package ru.point.financeapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.point.account.di.deps.AccountDepsStore
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.categories.di.deps.CategoriesDepsStore
import ru.point.financeapp.di.component.AppComponent
import ru.point.financeapp.di.component.DaggerAppComponent
import ru.point.impl.repository.AccountRepositoryImpl
import ru.point.transactions.di.TransactionDepsStore
import ru.point.utils.common.Result
import ru.point.utils.events.SnackbarEvents
import ru.point.utils.model.toUserMessage
import ru.point.utils.network.NetworkHolder
import javax.inject.Inject

class App : Application() {
    lateinit var appComponent: AppComponent

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var accountPrefs: AccountPreferencesRepo

    @Inject
    lateinit var accountRepo: AccountRepositoryImpl

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().context(this).build()

        AccountDepsStore.accountDeps = appComponent

        CategoriesDepsStore.categoriesDeps = appComponent

        TransactionDepsStore.transactionDeps = appComponent

        appComponent.inject(this)

        NetworkHolder.init(connectivityManager = getSystemService(ConnectivityManager::class.java))

        appScope.launch {
            val storedId = accountPrefs.accountIdFlow.firstOrNull()
            if (storedId != null) {
                return@launch
            }

            accountRepo.observe()
                .collectLatest { result ->
                    when (result) {
                        is Result.Loading -> {
                        }

                        is Result.Error -> {
                            val msg = result.cause.toUserMessage()
                            SnackbarEvents.post("Не удалось получить userId: $msg")
                        }

                        is Result.Success -> {
                            val account = result.data
                            accountPrefs.saveAccountId(account.id)
                            accountPrefs.saveCurrency(account.currency)
                        }
                    }
                }
        }
    }
}

val Context.appComponent: AppComponent
    get() =
        when (this) {
            is App -> appComponent
            else -> applicationContext.appComponent
        }
