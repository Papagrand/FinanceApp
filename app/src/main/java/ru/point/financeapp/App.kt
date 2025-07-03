package ru.point.financeapp

import android.app.Application
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.point.financeapp.di.AppComponent
import ru.point.financeapp.di.DaggerAppComponent
import ru.point.impl.repository.AccountRepositoryImpl
import ru.point.utils.common.AccountPreferences
import ru.point.utils.common.Result
import ru.point.utils.events.SnackbarEvents
import ru.point.utils.model.AppError
import ru.point.utils.network.NetworkHolder
import javax.inject.Inject

class App : Application() {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var accountPrefs: AccountPreferences

    @Inject
    lateinit var accountRepo: AccountRepositoryImpl

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // TODO Потом сделать Инжект NetworkTracker в экраны (через ViewModel или напрямую в Composable), вместо обращения к NetworkHolder.tracker.
        NetworkHolder.init(this)

        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)

        accountPrefs = AccountPreferences(this)

        appScope.launch {
            accountRepo.observe()
                .collectLatest { result ->
                    when (result) {
                        is Result.Loading -> {
                        }
                        is Result.Error -> {
                            val msg =
                                when (val cause = result.cause) {
                                    AppError.BadRequest -> "Неверный формат данных"
                                    AppError.Unauthorized -> "Неавторизованный доступ"
                                    AppError.NoInternet -> "Нет подключения к интернету"
                                    is AppError.ServerError -> "Сервер временно недоступен"
                                    is AppError.Http -> "HTTP ${cause.code}: ${cause.body ?: "Ошибка"}"
                                    else -> "Неизвестная ошибка"
                                }
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

    override fun onTerminate() {
        NetworkHolder.tracker.release()
        super.onTerminate()
    }
}

val Context.appComponent: AppComponent
    get() = (applicationContext as App).appComponent
