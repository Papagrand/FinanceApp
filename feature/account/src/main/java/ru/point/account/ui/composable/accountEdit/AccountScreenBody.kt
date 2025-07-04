package ru.point.account.ui.composable.accountEdit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.point.account.R
import ru.point.account.ui.mvi.accountEdit.AccountEditState
import ru.point.utils.extensionsAndParsers.toCurrencySymbol

@Composable
fun AccountEditScreenBody(
    state: AccountEditState,
    onNameChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currencies =
        listOf(
            CurrencyUi("₽", "Российский рубль ₽", R.drawable.ruble),
            CurrencyUi("$", "Американский доллар $", R.drawable.dollar),
            CurrencyUi("€", "Евро €", R.drawable.euro),
        )

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        AccountNameRow(
            value = state.name,
            onChange = onNameChange,
        )
        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )
        BalanceRow(
            balance = state.balance,
            isError = !state.balanceValid,
            errorText = state.balanceError,
            onChange = onBalanceChange,
        )
        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )
        CurrencyRow(
            current = state.currency.toCurrencySymbol(),
            allCurrencies = currencies,
            onSelect = onCurrencyChange,
        )
        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )
    }
}
