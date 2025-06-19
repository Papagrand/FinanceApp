package ru.point.financeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.point.account.presentation.ui.AccountScreen
import ru.point.expenses.presentation.ui.ExpensesScreen
import ru.point.income.presentation.ui.IncomeScreen
import ru.point.categories.presentation.ui.CategoryScreen
import ru.point.settings.presentation.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = NavRoute.Expenses.route) {
        composable(NavRoute.Expenses.route) { ExpensesScreen() }
        composable(NavRoute.Income.route) { IncomeScreen() }
        composable(NavRoute.Account.route) { AccountScreen() }
        composable(NavRoute.Category.route) { CategoryScreen() }
        composable(NavRoute.Settings.route) { SettingsScreen() }
    }
}