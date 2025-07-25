package ru.point.settings.mainColorScreen.ui.mvi

internal sealed interface MainColorIntent {
    data class PickNewColor(val name: String) : MainColorIntent}