package ru.point.transactions.history.ui.composable.composableFunctions

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import ru.point.api.model.TransactionDto
import ru.point.transactions.R
import ru.point.ui.composables.BaseListItem
import ru.point.utils.extensionsAndParsers.toCurrencySymbol
import ru.point.utils.extensionsAndParsers.toPrettyNumber

@Composable
internal fun HistoryRow(
    modifier: Modifier,
    historyItem: TransactionDto,
    onClick: () -> Unit = {},
) = BaseListItem(
    rowHeight = 70.dp,
    onClick = onClick,
    modifier =
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    lead = {
        val iconText = historyItem.emoji

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
                    MaterialTheme.typography.bodyLarge,
            )
        }
    },
    content = {
        Text(
            text = historyItem.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (historyItem.comment != null) {
            Text(
                text = historyItem.comment!!,
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