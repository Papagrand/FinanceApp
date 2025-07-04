package ru.point.financeapp

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.point.financeapp.di.AppComponent
import ru.point.financeapp.di.DaggerAppComponent
import ru.point.impl.repository.AccountRepositoryImpl
import ru.point.utils.common.AccountPreferences
import ru.point.utils.common.Result
import ru.point.utils.events.SnackbarEvents
import ru.point.utils.model.AppError
import javax.inject.Inject

class App : Application() {
    lateinit var appComponent: AppComponent
        private set

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var accountPrefs: AccountPreferences

    @Inject
    lateinit var accountRepo: AccountRepositoryImpl

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)

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
}
