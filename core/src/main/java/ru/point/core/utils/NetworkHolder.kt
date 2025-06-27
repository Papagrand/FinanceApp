package ru.point.core.utils

import android.annotation.SuppressLint
import android.content.Context

object NetworkHolder {
    @SuppressLint("StaticFieldLeak")
    lateinit var tracker: NetworkTracker
        private set

    fun init(context: Context) {
        tracker = NetworkTracker(context.applicationContext)
    }
}
