package ru.point.financeapp.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.point.account.presentation.ui.AccountScreen
import ru.point.categories.presentation.ui.CategoryScreen
import ru.point.expenses.presentation.ui.ExpensesScreen
import ru.point.financeapp.NavigatorImpl
import ru.point.history.presentation.ui.HistoryScreen
import ru.point.income.presentation.ui.IncomeScreen
import ru.point.settings.presentation.SettingsScreen

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
                enterTransition = { EnterTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
                exitTransition = { ExitTransition.None },
            ) {
                ExpensesScreen(navigator = navigator)
            }

            composable(
                NavRoute.History.route,
                enterTransition = { EnterTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
                exitTransition = { ExitTransition.None },
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
                enterTransition = { EnterTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
                exitTransition = { ExitTransition.None },
            ) {
                IncomeScreen(navigator = navigator)
            }
            composable(
                NavRoute.History.route,
                enterTransition = { EnterTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
                exitTransition = { ExitTransition.None },
            ) {
                HistoryScreen(
                    navigator = navigator,
                    isIncome = true,
                )
            }
        }

        composable(
            NavRoute.Account.route,
            enterTransition = { EnterTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            AccountScreen(navigator = navigator)
        }

        composable(
            NavRoute.Category.route,
            enterTransition = { EnterTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            CategoryScreen(navigator = navigator)
        }

        composable(
            NavRoute.Settings.route,
            enterTransition = { EnterTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            SettingsScreen(navigator = navigator)
        }
    }
}
