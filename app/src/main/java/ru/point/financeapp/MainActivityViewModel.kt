package ru.point.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import ru.point.api.flow.ThemePreferencesRepo

class MainActivityViewModel @Inject constructor(
    private val themePrefs: ThemePreferencesRepo
) : ViewModel() {
    private val _dataCollected = MutableStateFlow(false)
    val dataCollected = _dataCollected.asStateFlow()

    init {
        viewModelScope.launch {
            themePrefs.isDarkThemeFlow.first()

            themePrefs.primaryColorNameFlow.first()

            _dataCollected.value = true
        }
    }
}
