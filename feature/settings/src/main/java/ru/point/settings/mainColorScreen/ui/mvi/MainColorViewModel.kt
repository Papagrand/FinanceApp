package ru.point.settings.mainColorScreen.ui.mvi

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

internal class MainColorViewModel @Inject constructor(
    private val themeRepo: ThemePreferencesRepo
) : ViewModel() {

    private val _state = MutableStateFlow(MainColorState())
    val state: StateFlow<MainColorState> = _state.asStateFlow()

    private val intents = MutableSharedFlow<MainColorIntent>(extraBufferCapacity = 1)

    init {
        viewModelScope.launch {
            themeRepo.primaryColorNameFlow
                .collectLatest { color ->
                    _state.value = MainColorState(pickedColor = color)
                }
        }

        viewModelScope.launch {
            intents.collect { intent ->
                when (intent) {
                    is MainColorIntent.PickNewColor -> {
                        themeRepo.savePrimaryColor(intent.name)
                        _state.update { it.copy(pickedColor = intent.name) }
                    }
                }
            }
        }
    }

    fun dispatch(intent: MainColorIntent) = intents.tryEmit(intent)


}