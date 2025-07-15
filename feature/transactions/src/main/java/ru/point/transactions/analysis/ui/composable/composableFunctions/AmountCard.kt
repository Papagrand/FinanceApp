package ru.point.transactions.analysis.ui.composable.composableFunctions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.point.ui.R
import ru.point.ui.composables.BaseListItem
import ru.point.utils.extensionsAndParsers.toCurrencySymbol

@Composable
internal fun AmountCard(
    amountValue: String,
    currency: String,
    modifier: Modifier = Modifier
) {

    BaseListItem(
        modifier = modifier,
        content = {
            Text(
                text = stringResource(R.string.amount_placeholder)
            )
        },
        trail = {
            Text(
                text = "$amountValue ${currency.toCurrencySymbol()}"
            )
        }
    )
}