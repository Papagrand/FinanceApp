package ru.point.financeapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import ru.point.financeapp.ui.navigation.NavGraph

@Composable
fun MainActivityUI(viewModel: MainActivityViewModel) {

    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()
    val hierarchy = currentDestination?.destination

    val items = listOf(
        BottomNavigationItem(
            title = stringResource(R.string.expenses),
            icon  = ImageVector.vectorResource(R.drawable.expenses_icon),
            route = "expenses"
        ),
        BottomNavigationItem(
            title = stringResource(R.string.income),
            icon  = ImageVector.vectorResource(R.drawable.income_icon),
            route = "income"
        ),
        BottomNavigationItem(
            title = stringResource(R.string.account),
            icon  = ImageVector.vectorResource(R.drawable.account_icon),
            route = "account"
        ),
        BottomNavigationItem(
            title = stringResource(R.string.selection),
            icon  = ImageVector.vectorResource(R.drawable.selection_icon),
            route = "selection"
        ),
        BottomNavigationItem(
            title = stringResource(R.string.settings),
            icon  = ImageVector.vectorResource(R.drawable.settings_icon),
            route = "settings"
        )
    )

    Scaffold(
        bottomBar = {
            BottomBar(
                items = items,
                currentDestination = hierarchy,
                onItemClick = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface (
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            NavGraph(navController)
        }
    }
}