package ru.point.expenses.presentation.ui.composableFunctions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.point.core.ui.BaseListItem
import ru.point.core.utils.toCurrencySymbol
import ru.point.core.utils.toPrettyNumber
import ru.point.domain.model.Transaction
import ru.point.expenses.R

@Composable
fun ExpenseRow(
    modifier: Modifier,
    expense: Transaction,
    onClick: () -> Unit = {},
) = BaseListItem(
    rowHeight = 70.dp,
    onClick = onClick,
    modifier =
        modifier
            .fillMaxWidth()
            .clickable(onClick = { })
            .padding(horizontal = 16.dp),
    lead = {
        val initials = remember(expense.categoryName) { initialsOf(expense.categoryName) }
        val iconText = expense.emoji ?: initials

        Box(
            modifier =
                Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = iconText,
                style =
                    if (expense.emoji != null) {
                        MaterialTheme.typography.bodyLarge
                    } else {
                        TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            lineHeight = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 0.0.sp,
                        )
                    },
            )
        }
    },
    content = {
        Text(
            text = expense.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (expense.comment != "") {
            Text(
                text = expense.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    },
    trail = {
        Text(
            text = "${expense.amount.toPrettyNumber()} ${expense.currency.toCurrencySymbol()}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.right_arrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp),
        )
    },
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
