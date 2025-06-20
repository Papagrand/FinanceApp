package ru.point.navigation

sealed interface Route {
    data object Expenses : Route
    data object ExpensesHistory : Route
    data object IncomesHistory : Route
    data object Income : Route
    data object Account : Route
    data object Category : Route
    data object Settings : Route
}

interface Navigator {

    fun navigate(route: Route)

    fun popBackStack()

}