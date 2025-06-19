package ru.point.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkTracker(private val context: Context) {

    private val cm = context.getSystemService(ConnectivityManager::class.java)
    private val _online = MutableStateFlow(isOnline())
    val online: StateFlow<Boolean> = _online

    private val cb = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _online.value = true
        }

        override fun onLost(network: Network) {
            _online.value = false
        }

        override fun onCapabilitiesChanged(n: Network, c: NetworkCapabilities) {
            _online.value = c.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }

    init {
        cm.registerDefaultNetworkCallback(cb)
    }

    fun awaitInternetBlocking(timeoutMs: Long): Boolean {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            if (online.value) return true
            Thread.sleep(250)
        }
        return false
    }

    fun release() {
        runCatching { cm.unregisterNetworkCallback(cb) }
    }

    private fun isOnline(): Boolean =
        cm.activeNetwork?.let {
            cm.getNetworkCapabilities(it)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } == true
}