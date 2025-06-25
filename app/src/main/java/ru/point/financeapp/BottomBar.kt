package ru.point.financeapp

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun BottomBar(
    items: List<BottomNavigationItem>,
    currentDestination: NavDestination?,
    onItemClick: (String) -> Unit,
) {
    NavigationBar(Modifier) {
        items.forEach { item ->
            val selected =
                currentDestination
                    ?.hierarchy
                    ?.any { it.route == item.route } == true

            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.route) },
                icon = { Icon(item.icon, contentDescription = null) },
                label = {
                    Text(
                        text = item.title,
                        fontWeight = if (selected) FontWeight.W600 else FontWeight.W500,
                    )
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
            )
        }
    }
}
