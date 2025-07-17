package ru.point.transactions.analysis.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.navigation.Navigator
import ru.point.transactions.R
import ru.point.transactions.analysis.di.DaggerAnalysisTransactionsComponent
import ru.point.transactions.analysis.ui.composable.composableFunctions.AmountCard
import ru.point.transactions.analysis.ui.composable.composableFunctions.AnalysisCategoryRow
import ru.point.transactions.analysis.ui.composable.composableFunctions.AnalysisDatePicker
import ru.point.transactions.analysis.ui.composable.composableFunctions.AnalysisDonutChart
import ru.point.transactions.analysis.ui.mvi.AnalysisTransactionsEffect
import ru.point.transactions.analysis.ui.mvi.AnalysisTransactionsIntent
import ru.point.transactions.analysis.ui.mvi.AnalysisTransactionsViewModel
import ru.point.transactions.di.TransactionDepsStore
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BackAction
import ru.point.ui.composables.BackState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState
import ru.point.ui.composables.NoInternetBanner
import ru.point.ui.di.LocalInternetTracker
import ru.point.utils.extensionsAndParsers.toPrettyNumber

@Composable
fun AnalysisTransactionsScreen(
    navigator: Navigator,
    isIncome: Boolean
) {
    val analysisTransactionsComponent =
        remember {
            DaggerAnalysisTransactionsComponent
                .builder()
                .deps(transactionDeps = TransactionDepsStore.transactionDeps)
                .isIncome(isIncome)
                .build()
        }

    val viewModel =
        viewModel<AnalysisTransactionsViewModel>(factory = analysisTransactionsComponent.analysisTransactionsViewModelFactory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val isOnline by LocalInternetTracker.current.online.collectAsState()

    val currency by viewModel.currency.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.dispatch(AnalysisTransactionsIntent.Load)
        viewModel.effect.collect { eff ->
            when (eff) {
                is AnalysisTransactionsEffect.ShowSnackbar ->
                    snackbarHostState.showSnackbar(eff.message)

                AnalysisTransactionsEffect.Finish -> navigator.popBackStack()
            }
        }
    }

    BaseScaffold(
        title = stringResource(R.string.analys),
        actionState = ActionState.Shown,
        topAppBarColor = MaterialTheme.colorScheme.background,
        backState = BackState.Shown,
        backAction = BackAction(onClick = navigator::popBackStack),
        fabState = FabState.Hidden,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->

        when {
            !isOnline -> NoInternetBanner()
            else -> {
                when {
                    state.isLoading ->
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(top = 32.dp),
                            contentAlignment = Alignment.TopCenter,
                        ) { CircularProgressIndicator() }

                    state.error != null -> {
                        Text(
                            text = "Не гол",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    else -> {

                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                        ) {

                            AnalysisDatePicker(
                                modifier = Modifier,
                                startIso = state.startDate,
                                endIso = state.endDate,
                                onStartChanged = { viewModel.dispatch(AnalysisTransactionsIntent.StartDateChanged(it)) },
                                onEndChanged = { viewModel.dispatch(AnalysisTransactionsIntent.EndDateChanged(it)) }
                            )

                            HorizontalDivider(
                                modifier = Modifier,
                                color = MaterialTheme.colorScheme.surfaceDim,
                                thickness = 1.dp,
                            )

                            if (state.categorySummaries.isNotEmpty()) {
                                AmountCard(
                                    amountValue = state.grandAmountSummary.toString().toPrettyNumber(),
                                    currency = currency,
                                    modifier = Modifier
                                        .height(56.dp)
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                )

                                HorizontalDivider(
                                    modifier = Modifier,
                                    color = MaterialTheme.colorScheme.surfaceDim,
                                    thickness = 1.dp,
                                )

                                AnalysisDonutChart(
                                    summaries = state.categorySummaries,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )

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
                                    items(state.categorySummaries) { analysisTransactionItem ->
                                        AnalysisCategoryRow(
                                            modifier = Modifier,
                                            currency = currency,
                                            analysisTransactionItem,
                                        )

                                        HorizontalDivider(
                                            modifier = Modifier,
                                            color = MaterialTheme.colorScheme.surfaceDim,
                                            thickness = 1.dp,
                                        )
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.empty_analysis_data),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                    }
                }


            }
        }

    }
}