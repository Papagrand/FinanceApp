package ru.point.financeapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.point.financeapp.navigation.NavGraph
import ru.point.financeapp.navigation.NavRoute
import ru.point.navigation.Navigator
import ru.point.navigation.Route
import ru.point.ui.BottomBar
import ru.point.ui.BottomNavigationItem
import ru.point.utils.events.SnackbarEvents

@Composable
fun MainActivityUI(viewModel: MainActivityViewModel) {
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()
    val hierarchy = currentDestination?.destination

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        SnackbarEvents.messages.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    val items =
        listOf(
            BottomNavigationItem(
                title = stringResource(R.string.expenses),
                icon = ImageVector.vectorResource(R.drawable.expenses_icon),
                route = "expenses_graph",
            ),
            BottomNavigationItem(
                title = stringResource(R.string.income),
                icon = ImageVector.vectorResource(R.drawable.income_icon),
                route = "incomes_graph",
            ),
            BottomNavigationItem(
                title = stringResource(R.string.account),
                icon = ImageVector.vectorResource(R.drawable.account_icon),
                route = "account",
            ),
            BottomNavigationItem(
                title = stringResource(R.string.category),
                icon = ImageVector.vectorResource(R.drawable.selection_icon),
                route = "category",
            ),
            BottomNavigationItem(
                title = stringResource(R.string.settings),
                icon = ImageVector.vectorResource(R.drawable.settings_icon),
                route = "settings",
            ),
        )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            BottomBar(
                items = items,
                currentDestination = hierarchy,
                onItemClick = { graphRoute ->
                    navController.navigate(graphRoute) {
                        popUpTo(graphRoute) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { innerPadding ->
        Surface(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background,
        ) {
            NavGraph(navController = navController)
        }
    }
}

class NavigatorImpl(private val navController: NavHostController) : Navigator {
    override fun navigate(route: Route) {
        navController.navigate(route.toNavRoute().route)
    }

    override fun popBackStack() {
        navController.popBackStack()
    }
}

fun Route.toNavRoute(): NavRoute =
    when (this) {
        Route.Account -> NavRoute.Account
        Route.Category -> NavRoute.Category
        Route.Expenses -> NavRoute.Expenses
        Route.History -> NavRoute.History
        Route.Income -> NavRoute.Income
        Route.Settings -> NavRoute.Settings
    }
