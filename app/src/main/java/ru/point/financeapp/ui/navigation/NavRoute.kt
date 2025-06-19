package ru.point.financeapp.ui.navigation

sealed class NavRoute(val route: String) {
    data object Expenses : NavRoute("expenses")
    data object Income : NavRoute("income")
    data object Account : NavRoute("account")
    data object Category : NavRoute("category")
    data object Settings : NavRoute("settings")

    companion object {
        val bottomDestinations = listOf(
            Expenses, Income, Account, Category, Settings
        )
    }
}
