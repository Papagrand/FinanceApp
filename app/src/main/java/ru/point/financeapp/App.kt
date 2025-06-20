package ru.point.financeapp

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.point.account.data.repositoryImpl.AccountRepositoryImpl
import ru.point.core.common.AccountPreferences
import ru.point.core.utils.NetworkHolder
import ru.point.network.client.RetrofitProvider
import ru.point.core.common.Result
import ru.point.core.error.AppError
import ru.point.financeapp.events.SnackbarEvents

class App : Application() {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var accountPrefs: AccountPreferences

    private lateinit var accountRepo: AccountRepositoryImpl

    override fun onCreate() {
        super.onCreate()
        NetworkHolder.init(this)
        RetrofitProvider.init(NetworkHolder.tracker)

        accountPrefs = AccountPreferences(this)

        accountRepo = AccountRepositoryImpl()

        appScope.launch {
            accountRepo.observe()
                .collectLatest { result ->
                    when (result) {
                        is Result.Loading -> {
                        }
                        is Result.Error -> {
                            val msg = when (val cause = result.cause) {
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