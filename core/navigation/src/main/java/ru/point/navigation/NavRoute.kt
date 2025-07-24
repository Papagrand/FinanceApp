package ru.point.navigation

/**
 * NavRoute
 * Чистый набор экранов приложения в виде sealed-класса.
 *
 * Каждая константа хранит строку route для NavHost.
 * bottomDestinations используется для BottomNavigationBar.
 */

sealed class NavRoute(val route: String) {
    data object Expenses : NavRoute("expenses")

    data object Category : NavRoute("category")

    data object Settings : NavRoute("settings")

    data object Income : NavRoute("income")

    data object Account : NavRoute("account")

    data object History : NavRoute("history/{isIncome}") {
        fun create(isIncome: Boolean) = "history/$isIncome"

        const val ARG_IS_INCOME = "isIncome"
    }

    data object AnalysisTransactions : NavRoute("analysisTransactions/{isIncome}"){
        fun create(isIncome: Boolean) = "analysisTransactions/$isIncome"

        const val ARG_IS_INCOME = "isIncome"
    }

    data object AddOrEditTransaction :
        NavRoute("addOrEditTransaction?transactionId={transactionId}&isIncome={isIncome}") {
        fun create(
            transactionId: Int = -1,
            isIncome: Boolean,
        ) = "addOrEditTransaction?transactionId=$transactionId&isIncome=$isIncome"

        const val ARG_ID = "transactionId"
        const val ARG_IS_INCOME = "isIncome"
    }

    data object AccountEdit : NavRoute("accountEdit")

    data object MainColor : NavRoute("mainColor")

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
