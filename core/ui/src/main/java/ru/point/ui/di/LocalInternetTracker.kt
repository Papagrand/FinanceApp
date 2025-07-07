package ru.point.ui.di

import androidx.compose.runtime.staticCompositionLocalOf
import ru.point.utils.network.NetworkTracker

val LocalInternetTracker =
    staticCompositionLocalOf<NetworkTracker> {
        error("No InternetTracker provided")
    }
