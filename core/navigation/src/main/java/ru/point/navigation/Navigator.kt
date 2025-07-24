package ru.point.navigation

sealed interface Route {
    data object Expenses : Route

    data class History(val isIncome: Boolean) : Route

    data class AnalysisTransactions(val isIncome: Boolean) : Route

    data class AddOrEditTransaction(
        val transactionId: Int? = null,
        val isIncome: Boolean,
    ) : Route

    data object Income : Route

    data object Account : Route

    data object AccountEdit : Route

    data object Category : Route

    data object Settings : Route

    data object MainColor : Route
}

interface Navigator {
    fun navigate(route: Route)

    fun popBack()

    fun navigateUp()
}
