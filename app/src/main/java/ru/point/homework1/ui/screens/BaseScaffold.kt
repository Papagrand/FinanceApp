package ru.point.homework1.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

data class TopBarAction(
    @DrawableRes val iconRes: Int,
    val contentDescription: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScaffold(
    title: String,
    modifier: Modifier = Modifier,
    action: TopBarAction? = null,
    actionState: ActionState = ActionState.Hidden,
    fabState: FabState = FabState.Hidden,
    onFabClick: () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
                title = { Text(title) },
                actions = {
                    AnimatedVisibility(visible = actionState is ActionState.Shown) {
                        action?.let { act ->
                            IconButton(onClick = act.onClick) {
                                Icon(
                                    painter = painterResource(act.iconRes),
                                    contentDescription = act.contentDescription,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
        floatingActionButton = {
            AnimatedVisibility(visible = fabState is FabState.Shown) {
                FloatingActionButton(
                    onClick = onFabClick,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    shape = CircleShape
                ) { Icon(Icons.Default.Add, contentDescription = null) }
            }
        },
        containerColor = containerColor,
        content = content
    )
}

sealed interface FabState {
    object Shown : FabState
    object Hidden : FabState
}

sealed interface ActionState {
    object Shown : ActionState
    object Hidden : ActionState
}