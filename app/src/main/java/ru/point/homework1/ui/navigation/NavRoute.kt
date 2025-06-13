package ru.point.homework1.ui.navigation

sealed class NavRoute(val route: String) {
    data object Expenses : NavRoute("expenses")
    data object Income : NavRoute("income")
    data object Account : NavRoute("account")
    data object Selection : NavRoute("selection")
    data object Settings : NavRoute("settings")

    companion object {
        val bottomDestinations = listOf(
            Expenses, Income, Account, Selection, Settings
        )
    }
}
