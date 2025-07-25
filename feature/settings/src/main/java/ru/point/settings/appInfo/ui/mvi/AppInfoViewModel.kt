package ru.point.settings.appInfo.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.point.settings.appInfo.domain.GetAppLastTimeUpdateUseCase
import ru.point.settings.appInfo.domain.GetAppVersionUseCase

internal class AppInfoViewModel @Inject constructor(
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val getAppLastTimeUpdateUseCase: GetAppLastTimeUpdateUseCase,
) : ViewModel() {

    private val intents = MutableSharedFlow<AppInfoIntent>(
        replay = 1,
        extraBufferCapacity = 1
    )

    private val _state = MutableStateFlow(AppInfoState())
    val state: StateFlow<AppInfoState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            intents.collect { intent ->
                when (intent) {
                    is AppInfoIntent.LoadVersion -> loadVersion()
                    is AppInfoIntent.LoadLastUpdate -> loadLastTimeUpdate()
                }
            }
        }
    }

    fun dispatch(intent: AppInfoIntent) = intents.tryEmit(intent)

    private fun loadVersion() {
        viewModelScope.launch {
            val version = getAppVersionUseCase()
            _state.update { it.copy(appVersion = version) }
        }
    }

    private fun loadLastTimeUpdate() {
        viewModelScope.launch {
            val last = getAppLastTimeUpdateUseCase()
            _state.update { it.copy(lastTimeUpdate = last) }
        }
    }
}