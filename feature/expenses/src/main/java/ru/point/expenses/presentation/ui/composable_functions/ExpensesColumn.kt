package ru.point.expenses.presentation.ui.composable_functions

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
import ru.point.core.utils.toCurrencySymbol
import ru.point.core.utils.toPrettyNumber
import ru.point.domain.model.TransactionPlaceHolder
import ru.point.expenses.presentation.mvi.expenses.ExpensesState


@Composable
fun ExpensesColumn(
    innerPadding: PaddingValues,
    state: ExpensesState,
    placeholder: TransactionPlaceHolder
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        when {
            state.isLoading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) { CircularProgressIndicator() }

            state.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),

                    ) {
                    TotalExpensesToday(
                        modifier = Modifier,
                        total = "${placeholder.amount.toPrettyNumber()} ${placeholder.currency.toCurrencySymbol()}"
                    )
                    HorizontalDivider(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        thickness = 1.dp
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "${state.error}",
                    )
                }

            }

            else -> {
                if (state.list.isNotEmpty()) {
                    TotalExpensesToday(
                        modifier = Modifier,
                        total = "${state.total.toString().toPrettyNumber()} ${state.list[0].currency.toCurrencySymbol()}"
                    )
                } else {
                    TotalExpensesToday(
                        modifier = Modifier,
                        total = "${placeholder.amount.toPrettyNumber()} ${placeholder.currency.toCurrencySymbol()}"
                    )
                }
                HorizontalDivider(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.surfaceDim,
                    thickness = 1.dp
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(state.list) { expense ->
                        ExpenseRow(
                            modifier = Modifier,
                            expense
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
}