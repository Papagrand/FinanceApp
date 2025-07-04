package ru.point.financeapp.di

import android.content.Context
import ru.point.financeapp.App

val Context.appComponent: AppComponent
    get() = (applicationContext as App).appComponent
