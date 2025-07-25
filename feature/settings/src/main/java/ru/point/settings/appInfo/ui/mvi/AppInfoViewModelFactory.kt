package ru.point.settings.appInfo.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import ru.point.settings.appInfo.domain.GetAppLastTimeUpdateUseCase
import ru.point.settings.appInfo.domain.GetAppVersionUseCase

internal class AppInfoViewModelFactory @Inject constructor(
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val getAppLastTimeUpdateUseCase: GetAppLastTimeUpdateUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AppInfoViewModel(
            getAppVersionUseCase = getAppVersionUseCase,
            getAppLastTimeUpdateUseCase = getAppLastTimeUpdateUseCase,
        ) as T
    }
}