package ru.point.financeapp

import android.app.Application
import ru.point.core.utils.NetworkHolder
import ru.point.network.client.RetrofitProvider

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkHolder.init(this)
        RetrofitProvider.init(NetworkHolder.tracker)
    }

    override fun onTerminate() {
        NetworkHolder.tracker.release()
        super.onTerminate()
    }
}