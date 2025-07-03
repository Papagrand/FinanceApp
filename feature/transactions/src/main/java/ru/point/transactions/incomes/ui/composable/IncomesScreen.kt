package ru.point.transactions.incomes.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.api.model.TransactionPlaceHolder
import ru.point.navigation.Navigator
import ru.point.navigation.Route
import ru.point.transactions.R
import ru.point.transactions.incomes.ui.composable.composableFunctions.IncomeRow
import ru.point.transactions.incomes.ui.composable.composableFunctions.TotalIncomesToday
import ru.point.transactions.incomes.ui.mvi.IncomesEffect
import ru.point.transactions.incomes.ui.mvi.IncomesIntent
import ru.point.transactions.incomes.ui.mvi.IncomesViewModel
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState
import ru.point.ui.composables.NoInternetBanner
import ru.point.ui.composables.TopBarAction
import ru.point.ui.di.LocalViewModelFactory
import ru.point.utils.extensionsAndParsers.toCurrencySymbol
import ru.point.utils.extensionsAndParsers.toPrettyNumber
import ru.point.utils.network.NetworkHolder
import kotlin.collections.isNotEmpty

/**
 * IncomeScreen
 *
 * Ответственность:
 * - отображение экрана с доходами за сегодня: шапка, кнопка истории, список транзакций или индикатор загрузки/ошибки;
 * - отправка MVI-интентов (Load, Retry) и обработка эффектов (показывать Snackbar);
 * - навигация к истории доходов и реакция на нажатие FAB;
 * - показ баннера при отсутствии соединения.
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(
    navigator: Navigator,
    onAddClick: () -> Unit = {},
) {
    val viewModel: IncomesViewModel = viewModel(factory = LocalViewModelFactory.current)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val currency by viewModel.currency.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val tracker = remember { NetworkHolder.tracker }
    val placeholder = incomesPlaceholder(currency)

    LaunchedEffect(Unit) {
        viewModel.dispatch(IncomesIntent.Load)
        viewModel.effect.collect { eff ->
            if (eff is IncomesEffect.ShowSnackbar) snackbarHostState.showSnackbar(eff.message)
        }
    }

    BaseScaffold(
        title = stringResource(R.string.income_today),
        action =
            TopBarAction(
                iconRes = R.drawable.history,
                contentDescription = "История",
                onClick = {
                    navigator.navigate(Route.History)
                },
            ),
        actionState = ActionState.Shown,
        fabState = FabState.Shown,
        onFabClick = onAddClick,
        snackbarHostState = snackbarHostState,
    ) { innerPadding ->
        NoInternetBanner(tracker = tracker)

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
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
                            total = "${state.total.toString().toPrettyNumber()} ${state.list[0].currency.toCurrencySymbol()}",
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
}

private fun incomesPlaceholder(currency: String): TransactionPlaceHolder {
    val placeholder =
        TransactionPlaceHolder(
            amount = "0",
            currency = currency,
        )
    return placeholder
}
