package ru.point.homework1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.point.homework1.ui.screens.account.AccountScreen
import ru.point.homework1.ui.screens.expenses.ExpensesScreen
import ru.point.homework1.ui.screens.income.IncomeScreen
import ru.point.homework1.ui.screens.selection.SelectionScreen
import ru.point.homework1.ui.screens.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = NavRoute.Expenses.route) {
        composable(NavRoute.Expenses.route) { ExpensesScreen() }
        composable(NavRoute.Income.route) { IncomeScreen() }
        composable(NavRoute.Account.route) { AccountScreen() }
        composable(NavRoute.Selection.route) { SelectionScreen() }
        composable(NavRoute.Settings.route) { SettingsScreen() }
    }
}