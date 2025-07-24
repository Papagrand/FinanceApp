package ru.point.settings.mainColorScreen.mvi

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.point.api.flow.ThemePreferencesRepo

internal class MainColorViewModel @Inject constructor(
    private val themeRepo: ThemePreferencesRepo
) : ViewModel() {

    private val _state = MutableStateFlow(MainColorState())
    val state: StateFlow<MainColorState> = _state.asStateFlow()

    private val intents = MutableSharedFlow<MainColorIntent>(extraBufferCapacity = 1)

    init {

    }

    fun dispatch(intent: MainColorIntent) = intents.tryEmit(intent)

}