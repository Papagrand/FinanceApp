package ru.point.transactions.incomes.ui.composable.composableFunctions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.point.api.model.TransactionPlaceHolder
import ru.point.transactions.incomes.ui.mvi.IncomesState
import ru.point.utils.extensionsAndParsers.toCurrencySymbol
import ru.point.utils.extensionsAndParsers.toPrettyNumber

@Composable
fun IncomesColumn(
    innerPadding: PaddingValues,
    state: IncomesState,
    currency: String?,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
    ) {

        if (currency == null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) { CircularProgressIndicator() }
            return
        }

        val placeholder = TransactionPlaceHolder("0", currency)

        when {
            state.isLoading ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.TopCenter,
                ) { CircularProgressIndicator() }

            state.error != null -> {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                ) {
                    TotalIncomesToday(
                        modifier = Modifier,
                        total = "${placeholder.amount.toPrettyNumber()} ${placeholder.currency.toCurrencySymbol()}",
                    )
                    HorizontalDivider(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        thickness = 1.dp,
                    )
                    Text(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        text = "${state.error}",
                    )
                }
            }

            else -> {
                if (state.list.isNotEmpty()) {
                    TotalIncomesToday(
                        modifier = Modifier,
                        total = "${
                            state.total.toString().toPrettyNumber()
                        } ${state.list[0].currency.toCurrencySymbol()}",
                    )
                } else {
                    TotalIncomesToday(
                        modifier = Modifier,
                        total = "${placeholder.amount.toPrettyNumber()} ${placeholder.currency.toCurrencySymbol()}",
                    )
                }

                HorizontalDivider(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.surfaceDim,
                    thickness = 1.dp,
                )

                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                ) {
                    items(state.list) { income ->
                        IncomeRow(
                            modifier = Modifier,
                            income,
                        )
                        HorizontalDivider(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            thickness = 1.dp,
                        )
                    }
                }
            }
        }
    }
}