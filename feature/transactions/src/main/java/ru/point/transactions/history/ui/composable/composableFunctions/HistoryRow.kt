package ru.point.transactions.history.ui.composable.composableFunctions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import ru.point.api.model.TransactionDto
import ru.point.transactions.R
import ru.point.ui.composables.BaseListItem
import ru.point.utils.extensionsAndParsers.toCurrencySymbol
import ru.point.utils.extensionsAndParsers.toPrettyNumber
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistoryRow(
    modifier: Modifier,
    historyItem: TransactionDto,
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
        val initials =
            remember(historyItem.categoryName) { initialsOf(historyItem.categoryName) }
        val iconText = historyItem.emoji ?: initials

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
                    if (historyItem.emoji != null) {
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
            text = historyItem.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (historyItem.comment != "") {
            Text(
                text = historyItem.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    },
    trail = {
        val instant = Instant.parse(historyItem.dateTime)

        val dateTime =
            instant
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

        val transactionTime =
            dateTime.format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale("ru")),
            )

        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = "${historyItem.amount.toPrettyNumber()} ${historyItem.currency.toCurrencySymbol()}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = transactionTime,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
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
