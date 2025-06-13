package ru.point.homework1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.HomeWork1Theme
import ru.point.homework1.ui.navigation.NavGraph


data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.dataCollected.value
            }
        }

        enableEdgeToEdge()
        setContent {
            HomeWork1Theme {

                val navController = rememberNavController()

                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination

                val items = listOf(
                    BottomNavigationItem(
                        title = stringResource(R.string.expenses),
                        selectedIcon = ImageVector.vectorResource(R.drawable.expenses_icon),
                        unselectedIcon = ImageVector.vectorResource(R.drawable.expenses_icon),
                        route = "expenses"
                    ),
                    BottomNavigationItem(
                        title = stringResource(R.string.income),
                        selectedIcon = ImageVector.vectorResource(R.drawable.income_icon),
                        unselectedIcon = ImageVector.vectorResource(R.drawable.income_icon),
                        route = "income"
                    ),
                    BottomNavigationItem(
                        title = stringResource(R.string.account),
                        selectedIcon = ImageVector.vectorResource(R.drawable.account_icon),
                        unselectedIcon = ImageVector.vectorResource(R.drawable.account_icon),
                        route = "account"
                    ),
                    BottomNavigationItem(
                        title = stringResource(R.string.selection),
                        selectedIcon = ImageVector.vectorResource(R.drawable.selection_icon),
                        unselectedIcon = ImageVector.vectorResource(R.drawable.selection_icon),
                        route = "selection"
                    ),
                    BottomNavigationItem(
                        title = stringResource(R.string.settings),
                        selectedIcon = ImageVector.vectorResource(R.drawable.settings_icon),
                        unselectedIcon = ImageVector.vectorResource(R.drawable.settings_icon),
                        route = "settings"
                    )
                )


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar (
                                modifier = Modifier
                                    .navigationBarsPadding()
                            ) {

                                items.forEach { item ->
                                    val selected = currentDestination?.hierarchy
                                        ?.any { it.route == item.route } == true

                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                launchSingleTop = true
                                                restoreState = true
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                            }
                                        },
                                        label = { Text(item.title) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected)
                                                    item.selectedIcon else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
                                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    ) { padding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            NavGraph(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
