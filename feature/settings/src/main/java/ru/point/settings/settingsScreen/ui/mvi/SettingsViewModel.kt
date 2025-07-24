package ru.point.settings.settingsScreen.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.point.api.flow.ThemePreferencesRepo

internal class SettingsViewModel @Inject constructor(
    private val themeRepo: ThemePreferencesRepo
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val intents = MutableSharedFlow<SettingsIntent>(extraBufferCapacity = 1)

    init {
        viewModelScope.launch {
            themeRepo.isDarkThemeFlow
                .collectLatest { isDark ->
                    _state.value = SettingsState(isDarkTheme = isDark)
                }
        }

        viewModelScope.launch {
            intents.collectLatest { intent ->
                when (intent) {
                    SettingsIntent.ToggleDarkTheme -> {
                        val newValue = !_state.value.isDarkTheme
                        themeRepo.saveDarkTheme(newValue)
                        _state.update { it.copy(isDarkTheme = newValue) }
                    }
                }
            }
        }
    }

    fun dispatch(intent: SettingsIntent) = intents.tryEmit(intent)

    fun toggleDarkTheme() {
        val new = !_state.value.isDarkTheme
        viewModelScope.launch {
            themeRepo.saveDarkTheme(new)
        }
    }
}