package ru.point.income.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.income.R
import ru.point.core.utils.formatMoney
import ru.point.core.utils.moneyToLong
import ru.point.core.ui.ActionState
import ru.point.core.ui.BaseListItem
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.core.ui.NoInternetBanner
import ru.point.core.ui.TopBarAction
import ru.point.core.utils.NetworkHolder
import ru.point.core.utils.toCurrencySymbol
import ru.point.core.utils.toPrettyNumber
import ru.point.data.repositoryImpl.TransactionRepositoryImpl
import ru.point.domain.model.Transaction
import ru.point.domain.model.TransactionPlaceHolder
import ru.point.domain.usecase.GetExpensesTodayUseCase
import ru.point.domain.usecase.GetIncomesTodayUseCase
import ru.point.income.domain.IncomePlaceHolder
import ru.point.income.presentation.mvi.IncomesEffect
import ru.point.income.presentation.mvi.IncomesIntent
import ru.point.income.presentation.mvi.IncomesViewModel
import ru.point.income.presentation.mvi.IncomesViewModelFactory
import ru.point.network.client.RetrofitProvider


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(
    onAddClick: () -> Unit = {}
) {
    val repo = TransactionRepositoryImpl(RetrofitProvider.instance)
    val useCase = GetIncomesTodayUseCase(repo)
    val factory = remember { IncomesViewModelFactory(useCase) }
    val viewModel: IncomesViewModel = viewModel(factory = factory)

    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val tracker = remember { NetworkHolder.tracker }
    val placeholder = expensesPlaceholder()

    LaunchedEffect(Unit) {
        viewModel.dispatch(IncomesIntent.Load(65))
        viewModel.effect.collect { eff ->
            if (eff is IncomesEffect.ShowSnackbar) snackbarHostState.showSnackbar(eff.message)
        }
    }

    BaseScaffold(
        title = stringResource(R.string.income_today),
        action = TopBarAction(
            iconRes = R.drawable.history,
            contentDescription = "История",
            onClick = {}
        ),
        actionState = ActionState.Shown,
        fabState = FabState.Shown,
        onFabClick = onAddClick
    )
    { innerPadding ->
        NoInternetBanner(tracker = tracker)

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
                        TotalIncomesToday(
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
                        TotalIncomesToday(
                            modifier = Modifier,
                            total = "${state.total.toString().toPrettyNumber()} ${state.list[0].currency.toCurrencySymbol()}"
                        )
                    } else {
                        TotalIncomesToday(
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
                        items(state.list) { income ->
                            IncomeRow(
                                modifier = Modifier,
                                income,
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
}

@Composable
fun TotalIncomesToday(
    modifier: Modifier,
    total: String
) {
    Surface(
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BaseListItem(
            onClick = { },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            rowHeight = 56.dp,
            content = {
                Text(
                    text = stringResource(R.string.total),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trail = {
                Text(
                    total,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}

@Composable
fun IncomeRow(
    modifier: Modifier,
    income: Transaction,
    onClick: () -> Unit = {}
) = BaseListItem(
    rowHeight = 70.dp,
    onClick = onClick,
    modifier = modifier
        .fillMaxWidth()
        .clickable(onClick = { })
        .padding(horizontal = 16.dp),
    content = {
        Text(
            text = income.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    },
    trail = {
        Text(
            text = "${income.amount.toPrettyNumber()} ${income.currency.toCurrencySymbol()}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.right_arrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
)

private fun expensesPlaceholder(): TransactionPlaceHolder {
    val placeholder: TransactionPlaceHolder = TransactionPlaceHolder(
        amount = "0",
        currency = "RUB"
    )
    return placeholder
}
