package ru.point.financeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.point.account.ui.composable.account.AccountScreen
import ru.point.account.ui.composable.accountEdit.AccountEditScreen
import ru.point.categories.presentation.ui.CategoryScreen
import ru.point.financeapp.NavigatorImpl
import ru.point.navigation.NavRoute
import ru.point.settings.appInfo.ui.composables.AppInfoScreen
import ru.point.settings.mainColorScreen.ui.composables.MainColorScreen
import ru.point.settings.settingsScreen.ui.composables.SettingsScreen
import ru.point.transactions.addOrEditTransaction.ui.composable.AddOrEditTransactionScreen
import ru.point.transactions.analysis.ui.composable.AnalysisTransactionsScreen
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
                arguments =
                    listOf(
                        navArgument("isIncome") { type = NavType.BoolType },
                    ),
            ) { entry ->
                val isIncome = entry.arguments!!.getBoolean("isIncome")

                HistoryScreen(navigator, isIncome)
            }

            composable(
                NavRoute.AnalysisTransactions.route,
                arguments =
                    listOf(
                        navArgument("isIncome") { type = NavType.BoolType },
                    ),
            ) { entry ->
                val isIncome = entry.arguments!!.getBoolean("isIncome")

                AnalysisTransactionsScreen(navigator, isIncome)
            }

            composable(
                NavRoute.AddOrEditTransaction.route,
                arguments =
                    listOf(
                        navArgument("transactionId") {
                            type = NavType.IntType
                            defaultValue = -1
                        },
                        navArgument("isIncome") { type = NavType.BoolType },
                    ),
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments!!.getInt("transactionId")
                val isIncome = backStackEntry.arguments!!.getBoolean("isIncome")

                AddOrEditTransactionScreen(navigator, transactionId, isIncome)
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
                arguments =
                    listOf(
                        navArgument("isIncome") { type = NavType.BoolType },
                    ),
            ) { entry ->
                val isIncome = entry.arguments!!.getBoolean("isIncome")

                HistoryScreen(navigator, isIncome)
            }

            composable(
                NavRoute.AnalysisTransactions.route,
                arguments =
                    listOf(
                        navArgument("isIncome") { type = NavType.BoolType },
                    ),
            ) { entry ->
                val isIncome = entry.arguments!!.getBoolean("isIncome")

                AnalysisTransactionsScreen(navigator, isIncome)
            }

            composable(
                NavRoute.AddOrEditTransaction.route,
                arguments =
                    listOf(
                        navArgument("transactionId") {
                            type = NavType.IntType
                            defaultValue = -1
                        },
                        navArgument("isIncome") { type = NavType.BoolType },
                    ),
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments!!.getInt("transactionId")
                val isIncome = backStackEntry.arguments!!.getBoolean("isIncome")

                AddOrEditTransactionScreen(navigator, transactionId, isIncome)
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
            CategoryScreen()
        }

        navigation(
            startDestination = NavRoute.Settings.route,
            route = "settings_graph",
        ) {

            composable(
                NavRoute.Settings.route,
            ) {
                SettingsScreen(navigator = navigator)
            }

            composable(
                NavRoute.MainColor.route,
            ) {
                MainColorScreen(navigator = navigator)
            }

            composable(
                NavRoute.AppInfo.route,
            ) {
                AppInfoScreen(navigator = navigator)
            }

        }


    }
}
