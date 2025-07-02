package ru.point.financeapp.navigation

/**
 * NavRoute
 * Чистый набор экранов приложения в виде sealed-класса.
 *
 * Каждая константа хранит строку route для NavHost.
 * bottomDestinations используется для BottomNavigationBar.
 */

sealed class NavRoute(val route: String) {
    data object Expenses : NavRoute("expenses")

    data object History : NavRoute("history")

    data object Income : NavRoute("income")

    data object Account : NavRoute("account")

    data object Category : NavRoute("category")

    data object Settings : NavRoute("settings")

    companion object {
        val bottomDestinations =
            listOf(
                Expenses,
                Income,
                Account,
                Category,
                Settings,
            )
    }
}
