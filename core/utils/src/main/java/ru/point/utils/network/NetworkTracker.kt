package ru.point.utils.network

import kotlinx.coroutines.flow.StateFlow

interface NetworkTracker {
    val online: StateFlow<Boolean>
}
