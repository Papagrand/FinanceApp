package ru.point.homework1.ui.screens.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.point.homework1.R
import ru.point.homework1.ui.parsing_amount.formatMoney
import ru.point.homework1.ui.parsing_amount.moneyToLong
import ru.point.homework1.ui.screens.ActionState
import ru.point.homework1.ui.screens.BaseListItem
import ru.point.homework1.ui.screens.BaseScaffold
import ru.point.homework1.ui.screens.FabState
import ru.point.homework1.ui.screens.TopBarAction


@Preview()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    onAddClick: () -> Unit = {}
) {
    val expenses = remember { demoExpenses() }

    val total = remember(expenses) {
        expenses.sumOf { it.amount.moneyToLong() }
    }

    BaseScaffold(
        title = stringResource(R.string.expenses_today),
        action = TopBarAction(
            iconRes = R.drawable.history,
            contentDescription = "История",
            onClick = {}
        ),
        actionState = ActionState.Shown,
        fabState = FabState.Shown,
        onFabClick = onAddClick,
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            TotalExpensesToday(
                modifier = Modifier,
                total = total.formatMoney()
            )

            HorizontalDivider(
                modifier = Modifier,
                color = MaterialTheme.colorScheme.surfaceDim,
                thickness = 1.dp
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(expenses) { expense ->
                    ExpenseRow(
                        modifier = Modifier,
                        expense
                    )
                    HorizontalDivider(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}


@Composable
fun TotalExpensesToday(
    modifier: Modifier,
    total: String
) {
    Surface(
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BaseListItem(
            onClick = {  },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            rowHeight = 56.dp,
            lead = {  },
            content = {
                Text(
                    text  = stringResource(R.string.total),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trail = {
                Text(
                    total,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}

@Composable
fun ExpenseRow(
    modifier: Modifier,
    expense: Expense,
    onClick: () -> Unit = {}
) = BaseListItem(
    rowHeight = 70.dp,
    onClick = onClick,
    modifier = modifier
        .fillMaxWidth()
        .clickable(onClick = { })
        .padding(horizontal = 16.dp),
    lead = {
        val initials = remember(expense.title) { initialsOf(expense.title) }
        val iconText = expense.emojiIcon ?: initials

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iconText,
                style = if (expense.emojiIcon != null)
                    MaterialTheme.typography.bodyLarge
                else
                    TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.0.sp
                    )
            )
        }
    },

    content = {
        Text(
            text = expense.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        expense.subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    },

    trail = {
        Text(
            text = "${expense.amount} ${expense.currency}",
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


private fun demoExpenses(): List<Expense> = listOf(
    Expense(
        id = "0",
        title = "Аренда квартиры",
        subtitle = null,
        emojiIcon = "🏡",
        amount = "100 000",
        currency = "₽"

    ),
    Expense(
        id = "1",
        title = "Одежда",
        subtitle = null,
        emojiIcon = "👗",
        amount = "100 000",
        currency = "₽"
    ),
    Expense(
        id = "2",
        title = "На собачку",
        subtitle = "Джек",
        emojiIcon = "🐶",
        amount = "100 000",
        currency = "₽",

        ),
    Expense(
        id = "3",
        title = "На собачку",
        subtitle = "Энни",
        emojiIcon = "🐶",
        amount = "100 000",
        currency = "₽"
    ),
    Expense(
        id = "4",
        title = "Ремонт квартиры",
        subtitle = null,
        emojiIcon = null,
        amount = "100 000",
        currency = "₽"
    ),
    Expense(
        id = "5",
        title = "Продукты",
        subtitle = null,
        emojiIcon = "🍭",
        amount = "100 000",
        currency = "₽"
    ),
    Expense(
        id = "6",
        title = "Спортзал",
        subtitle = null,
        emojiIcon = "🏋️",
        amount = "100 000",
        currency = "₽"
    ),
    Expense(
        id = "7",
        title = "Медицина",
        subtitle = null,
        emojiIcon = "💊",
        amount = "100 000",
        currency = "₽"
    )
)

private fun initialsOf(title: String): String {
    val words = title.trim().split("\\s+".toRegex())
    val first = words.getOrNull(0)?.firstOrNull()?.uppercase() ?: ""
    val second = words.getOrNull(1)?.firstOrNull()?.uppercase() ?: ""
    return buildString {
        append(first)
        if (second.isNotEmpty()) append(second)
    }
}
