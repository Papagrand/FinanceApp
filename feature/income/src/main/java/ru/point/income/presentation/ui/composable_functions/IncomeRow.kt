package ru.point.income.presentation.ui.composable_functions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.point.core.ui.BaseListItem
import ru.point.core.utils.toCurrencySymbol
import ru.point.core.utils.toPrettyNumber
import ru.point.domain.model.Transaction
import ru.point.income.R

@Composable
fun IncomeRow(
    modifier: Modifier,
    income: Transaction,
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
            text = income.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    },
    trail = {
        Text(
            text = "${income.amount.toPrettyNumber()} ${income.currency.toCurrencySymbol()}",
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