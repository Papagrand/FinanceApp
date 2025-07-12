package ru.point.utils.network

import android.net.ConnectivityManager

object NetworkHolder {
    private var _tracker: NetworkTracker? = null
    val tracker
        get() =
            requireNotNull(_tracker) {
                "Tracker must be initialized"
            }

    fun init(connectivityManager: ConnectivityManager) {
        _tracker = NetworkTracker(connectivityManager = connectivityManager)
    }
}
