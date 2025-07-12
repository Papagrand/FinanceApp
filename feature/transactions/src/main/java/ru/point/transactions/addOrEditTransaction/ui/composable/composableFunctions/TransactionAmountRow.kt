package ru.point.transactions.addOrEditTransaction.ui.composable.composableFunctions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.point.ui.composables.SimpleListRow
import ru.point.utils.extensionsAndParsers.formatForDisplay

@Composable
fun TransactionAmountRow(
    amountRaw: String,
    onEdit: () -> Unit,
) {
    val display =
        remember(amountRaw) {
            formatForDisplay(
                raw = amountRaw,
                alwaysShowDecimals = true,
            )
        }

    SimpleListRow(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onEdit)
                .padding(horizontal = 16.dp),
        content = {
            Text(
                text = "Сумма",
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        trail = {
            Text(
                text = display.ifBlank { "0,00" },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End,
            )
        },
    )
}
