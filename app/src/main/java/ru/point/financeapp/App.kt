package ru.point.financeapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.point.account.di.deps.AccountDepsStore
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.repository.AccountRepository
import ru.point.api.repository.CategoryRepository
import ru.point.categories.di.deps.CategoriesDepsStore
import ru.point.financeapp.di.component.AppComponent
import ru.point.financeapp.di.component.DaggerAppComponent
import ru.point.impl.work.DaggerWorkerFactory
import ru.point.transactions.di.TransactionDepsStore
import ru.point.utils.common.Result
import ru.point.utils.events.SnackbarEvents
import ru.point.utils.model.toUserMessage

class App : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: DaggerWorkerFactory

    lateinit var appComponent: AppComponent

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO+ CoroutineExceptionHandler { _, t ->
        Log.e("AppScope", "Unhandled exception", t)
    })

    @Inject
    lateinit var accountPrefs: AccountPreferencesRepo

    @Inject
    lateinit var accountRepo: AccountRepository

    @Inject
    lateinit var categoryRepo: CategoryRepository


    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().context(this).build()

        initializeDeps()

        appComponent.inject(this)

        launchAppScope()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun launchAppScope() {
        appScope.launch {
            val storedId = accountPrefs.accountIdFlow.firstOrNull()
            if (storedId == null) {
                accountRepo.refreshFromRemote()
            }

            launch {
                var categoriesLoaded = false
                accountRepo.observe().collectLatest { result ->
                    when (result) {
                        is Result.Success -> with(accountPrefs) {
                            saveAccountId(result.data.id)
                            saveAccountName(result.data.name)
                            saveCurrency(result.data.currency)
                            if (!categoriesLoaded) {
                                categoriesLoaded = true
                                categoryRepo.refreshMyCategories(result.data.id)
                            }
                        }

                        is Result.Error -> SnackbarEvents.post(
                            "Не удалось получить userId: ${result.cause.toUserMessage()}"
                        )

                        else -> {} // Loading
                    }
                }
            }


            categoryRepo.refreshAllCategories()

        }
    }

    private fun initializeDeps() {

        AccountDepsStore.accountDeps = appComponent

        CategoriesDepsStore.categoriesDeps = appComponent

        TransactionDepsStore.transactionDeps = appComponent
    }

}

val Context.appComponent: AppComponent
    get() =
        when (this) {
            is App -> appComponent
            else -> applicationContext.appComponent
        }

