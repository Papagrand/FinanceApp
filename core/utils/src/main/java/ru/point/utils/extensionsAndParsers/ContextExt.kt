package ru.point.utils.extensionsAndParsers

import android.content.Context

fun Context.getAppVersionName(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName ?: "unknown"
}