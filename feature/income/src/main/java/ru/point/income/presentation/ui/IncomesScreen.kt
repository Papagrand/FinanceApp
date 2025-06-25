package ru.point.income.presentation.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.core.common.AccountPreferences
import ru.point.core.ui.ActionState
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.core.ui.NoInternetBanner
import ru.point.core.ui.TopBarAction
import ru.point.core.utils.NetworkHolder
import ru.point.core.utils.toCurrencySymbol
import ru.point.core.utils.toPrettyNumber
import ru.point.data.repositoryImpl.TransactionRepositoryImpl
import ru.point.domain.model.TransactionPlaceHolder
import ru.point.domain.usecase.GetIncomesTodayUseCase
import ru.point.income.R
import ru.point.income.presentation.mvi.incomes.IncomesEffect
import ru.point.income.presentation.mvi.incomes.IncomesIntent
import ru.point.income.presentation.mvi.incomes.IncomesViewModel
import ru.point.income.presentation.mvi.incomes.IncomesViewModelFactory
import ru.point.income.presentation.ui.composableFunctions.IncomeRow
import ru.point.income.presentation.ui.composableFunctions.TotalIncomesToday
import ru.point.navigation.Navigator
import ru.point.navigation.Route
import ru.point.network.client.RetrofitProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(
    navigator: Navigator,
    onAddClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val prefs = remember { AccountPreferences(context) }
    val repo = TransactionRepositoryImpl(RetrofitProvider.instance)
    val useCase = GetIncomesTodayUseCase(repo)
    val factory = remember { IncomesViewModelFactory(useCase, prefs) }
    val viewModel: IncomesViewModel = viewModel(factory = factory)

    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val tracker = remember { NetworkHolder.tracker }
    val placeholder = incomesPlaceholder()

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
                    navigator.navigate(Route.IncomesHistory)
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

private fun incomesPlaceholder(): TransactionPlaceHolder {
    val placeholder =
        TransactionPlaceHolder(
            amount = "0",
            currency = "RUB",
        )
    return placeholder
}
