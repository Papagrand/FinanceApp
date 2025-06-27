package ru.point.core.di

import javax.inject.Qualifier

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class IncomeHistory

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class ExpenseHistory
