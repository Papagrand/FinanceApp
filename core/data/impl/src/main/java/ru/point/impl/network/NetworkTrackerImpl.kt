package ru.point.impl.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.point.impl.work.PushAccountPendingWorker
import ru.point.impl.work.PushPendingWorker
import ru.point.utils.network.NetworkTracker

@Singleton
class NetworkTrackerImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val appContext: Context
) : NetworkTracker {
    private val _online = MutableStateFlow(isOnline())
    override val online: StateFlow<Boolean> = _online

    private val cb = object: ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _online.value = true
            triggerSync()
        }
        override fun onLost(network: Network) {
            _online.value = false
        }
        override fun onCapabilitiesChanged(n: Network, caps: NetworkCapabilities) {
            val has = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            if (has && !_online.value) {
                _online.value = true
                triggerSync()
            }
            else if (!has) _online.value = false
        }
    }

    init {
        connectivityManager.registerDefaultNetworkCallback(cb)
    }

    private fun isOnline(): Boolean =
        connectivityManager.activeNetwork
            ?.let { connectivityManager.getNetworkCapabilities(it) }
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false

    private fun triggerSync() {
        val conn = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        WorkManager.getInstance(appContext)
            .enqueueUniqueWork(
                "PushPending",
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<PushPendingWorker>()
                    .setConstraints(conn)
                    .addTag("sync_now")
                    .build()
            )

        WorkManager.getInstance(appContext)
            .enqueueUniqueWork(
                "PushAccountPending",
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<PushAccountPendingWorker>()
                    .setConstraints(conn)
                    .addTag("sync_now")
                    .build()
            )
    }
}