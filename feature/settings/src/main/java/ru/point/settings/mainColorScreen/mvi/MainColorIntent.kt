package ru.point.settings.mainColorScreen.mvi

internal sealed interface MainColorIntent {
    data class PickNewColor(val name: String) : MainColorIntent}