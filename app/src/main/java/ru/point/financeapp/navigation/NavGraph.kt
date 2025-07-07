package ru.point.financeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.point.account.ui.composable.account.AccountScreen
import ru.point.account.ui.composable.accountEdit.AccountEditScreen
import ru.point.categories.presentation.ui.CategoryScreen
import ru.point.financeapp.NavigatorImpl
import ru.point.settings.presentation.SettingsScreen
import ru.point.transactions.expenses.ui.composable.ExpensesScreen
import ru.point.transactions.history.ui.composable.HistoryScreen
import ru.point.transactions.incomes.ui.composable.IncomeScreen

/**
 * NavGraph
 * Определяет набор навигационных маршрутов и их связи.
 *
 * Главный NavHost со startDestination = "expenses_graph", два вложенных графа для расходов и доходов, а также
 * экраны аккаунта, категорий и настроек.
 */

@Composable
fun NavGraph(navController: NavHostController) {
    val navigator = remember { NavigatorImpl(navController) }
    NavHost(navController, startDestination = "expenses_graph") {
        navigation(
            startDestination = NavRoute.Expenses.route,
            route = "expenses_graph",
        ) {
            composable(
                NavRoute.Expenses.route,
            ) {
                ExpensesScreen(navigator = navigator)
            }

            composable(
                NavRoute.History.route,
            ) {
                HistoryScreen(
                    navigator = navigator,
                    isIncome = false,
                )
            }
        }

        navigation(
            startDestination = NavRoute.Income.route,
            route = "incomes_graph",
        ) {
            composable(
                NavRoute.Income.route,
            ) {
                IncomeScreen(navigator = navigator)
            }
            composable(
                NavRoute.History.route,
            ) {
                HistoryScreen(
                    navigator = navigator,
                    isIncome = true,
                )
            }
        }

        navigation(
            startDestination = NavRoute.Account.route,
            route = "account_graph",
        ) {
            composable(
                NavRoute.Account.route,
            ) {
                AccountScreen(navigator = navigator)
            }
            composable(
                NavRoute.AccountEdit.route,
            ) {
                AccountEditScreen(
                    navigator = navigator,
                )
            }
        }

        composable(
            NavRoute.Category.route,
        ) {
            CategoryScreen(navigator = navigator)
        }

        composable(
            NavRoute.Settings.route,
        ) {
            SettingsScreen(navigator = navigator)
        }
    }
}
