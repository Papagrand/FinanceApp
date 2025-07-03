package ru.point.account.ui.composable.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.point.account.R
import ru.point.api.model.AccountDto
import ru.point.ui.composables.BaseListItem
import ru.point.utils.extensionsAndParsers.toCurrencySymbol
import ru.point.utils.extensionsAndParsers.toPrettyNumber

/**
 * Balance
 *
 * Ответственность:
 * - отображение текущего баланса пользователя в списке;
 * - форматирование суммы и валюты через утилиты toPrettyNumber()/toCurrencySymbol();
 * - предоставление кликабельного UI с иконкой и значением баланса.
 *
 */

@Composable
internal fun Balance(
    modifier: Modifier,
    account: AccountDto,
    onClick: () -> Unit = {},
) {
    Surface(
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        BaseListItem(
            rowHeight = 56.dp,
            onClick = onClick,
            modifier =
                modifier
                    .fillMaxWidth()
                    .clickable(onClick = { })
                    .padding(horizontal = 16.dp),
            lead = {
                Box(
                    modifier =
                        Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "💰",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            },
            content = {
                Text(
                    text = stringResource(R.string.balance),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            trail = {
                Text(
                    text = "${account.balance.toPrettyNumber()} ${account.currency.toCurrencySymbol()}",
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
    }
}
