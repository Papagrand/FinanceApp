package ru.point.homework1.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.point.homework1.R
import ru.point.homework1.ui.screens.ActionState
import ru.point.homework1.ui.screens.BaseListItem
import ru.point.homework1.ui.screens.BaseScaffold
import ru.point.homework1.ui.screens.FabState
import ru.point.homework1.ui.screens.TopBarAction

@Preview()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onAddClick: () -> Unit = {}
) {
    val account = remember {
        Account(
            id = 123,
            userId = 1231231,
            name = "User",
            balance = "-670 000",
            currency = "₽",
            createdAt = "02.06.2025",
            updatedAt = "14.06.2025"
        )
    }

    BaseScaffold(
        title = stringResource(R.string.my_account),
        action = TopBarAction(
            iconRes = R.drawable.edit,
            contentDescription = "Редактировать",
            onClick = {}
        ),
        actionState = ActionState.Shown,
        fabState = FabState.Shown,
        onFabClick = onAddClick,
    )  { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            ) {

            Balance(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                account
            )

            HorizontalDivider(
                modifier = Modifier,
                color = MaterialTheme.colorScheme.surfaceDim,
                thickness = 1.dp
            )

            Currency(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

        }
    }
}


@Composable
private fun Balance(
    modifier: Modifier,
    account: Account,
    onClick: () -> Unit = {},
) {
    Surface(
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BaseListItem(
            rowHeight = 56.dp,
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = { })
                .padding(horizontal = 16.dp),
            lead = {

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💰",
                        style = MaterialTheme.typography.bodyLarge

                    )
                }
            },

            content = {
                Text(
                    text = stringResource(R.string.balance),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },

            trail = {
                Text(
                    text = "${account.balance} ${account.currency}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.right_arrow),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}


@Composable
fun Currency(
    modifier: Modifier,
    onClick: () -> Unit = {},
) {
    Surface(
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BaseListItem(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = { })
                .padding(horizontal = 16.dp),
            rowHeight = 56.dp,
            content = {
                Text(
                    text  = stringResource(R.string.currency),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trail = {
                Text(
                    "₽",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.right_arrow),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}