package ru.point.income.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.point.income.R
import ru.point.core.utils.formatMoney
import ru.point.core.utils.moneyToLong
import ru.point.core.ui.ActionState
import ru.point.core.ui.BaseListItem
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.core.ui.TopBarAction
import ru.point.income.domain.Income


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(
    onAddClick: () -> Unit = {}
) {
    val incomes = remember { demoIncomes() }

    val total = remember(incomes) {
        incomes.sumOf { it.amount.moneyToLong() }
    }

    BaseScaffold(
        title = stringResource(R.string.income_today),
        action = TopBarAction(
            iconRes = R.drawable.history,
            contentDescription = "История",
            onClick = {}
        ),
        actionState = ActionState.Shown,
        fabState = FabState.Shown,
        onFabClick = onAddClick
    )
    { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            TotalIncomesToday(
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
                items(incomes) { income ->
                    IncomeRow(
                        modifier = Modifier,
                        income,
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
fun TotalIncomesToday(
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
fun IncomeRow(
    modifier: Modifier,
    income: Income,
    onClick: () -> Unit = {}
) = BaseListItem(
    rowHeight = 70.dp,
    onClick = onClick,
    modifier = modifier
        .fillMaxWidth()
        .clickable(onClick = { })
        .padding(horizontal = 16.dp),
    content = {
        Text(
            text = income.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    },
    trail = {
        Text(
            text = "${income.amount} ${income.currency}",
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

private fun demoIncomes(): List<Income> = listOf(
    Income(
        id = "0",
        title = "Зарплата",
        amount = "100 000",
        currency = "₽"

    ),
    Income(
        id = "1",
        title = "Подработка",
        amount = "100 000",
        currency = "₽"
    )
)