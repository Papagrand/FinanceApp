package ru.point.utils.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkTracker(private val connectivityManager: ConnectivityManager) {
    private val _online = MutableStateFlow(isOnline())
    val online: StateFlow<Boolean> = _online

    private val networkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _online.value = true
            }

            override fun onLost(network: Network) {
                _online.value = false
            }

            override fun onCapabilitiesChanged(
                n: Network,
                c: NetworkCapabilities,
            ) {
                _online.value = c.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
        }

    init {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private fun isOnline(): Boolean =
        connectivityManager.activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } == true
}
