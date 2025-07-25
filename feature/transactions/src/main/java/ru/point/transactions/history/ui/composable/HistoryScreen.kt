package ru.point.transactions.history.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
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
import ru.point.navigation.Route
import ru.point.transactions.R
import ru.point.transactions.di.TransactionDepsStore
import ru.point.transactions.history.di.DaggerHistoryComponent
import ru.point.transactions.history.ui.composable.composableFunctions.HistoryRow
import ru.point.transactions.history.ui.mvi.HistoryEffect
import ru.point.transactions.history.ui.mvi.HistoryIntent
import ru.point.transactions.history.ui.mvi.HistoryViewModel
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BackAction
import ru.point.ui.composables.BackState
import ru.point.ui.composables.BaseHistoryTopColumnPlaceholder
import ru.point.ui.composables.BaseHistoryTopElement
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState
import ru.point.ui.composables.NoInternetBanner
import ru.point.ui.composables.TopBarAction
import ru.point.ui.di.LocalInternetTracker
import ru.point.utils.extensionsAndParsers.toCurrencySymbol
import ru.point.utils.extensionsAndParsers.toPrettyNumber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.isNotEmpty

/**
 * HistoryScreen
 *
 * Ответственность:
 * - отображение экрана истории с доходами или расходами: шапка, кнопка анализа, данные или индикатор загрузки/ошибки;
 * - отправка MVI-интентов (Load, Retry) и обработка эффектов (показывать Snackbar);
 * - навигация к истории доходов и реакция на нажатие FAB;
 * - показ баннера при отсутствии соединения.
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navigator: Navigator,
    isIncome: Boolean,
) {
    val historyComponent =
        remember {
            DaggerHistoryComponent
                .builder()
                .deps(transactionDeps = TransactionDepsStore.transactionDeps)
                .build()
        }

    val viewModel = viewModel<HistoryViewModel>(factory = historyComponent.historyViewModelFactory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val isOnline by LocalInternetTracker.current.online.collectAsState()

    val currency by viewModel.currency.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val monthYear =
        LocalDate.now()
            .format(DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru")))
            .replaceFirstChar { it.uppercase() }

    val nowWithTime =
        LocalDateTime.now()
            .format(
                DateTimeFormatter.ofPattern(
                    "dd.MM.yyyy HH:mm",
                    Locale("ru"),
                ),
            )

    LaunchedEffect(isIncome) {
        viewModel.dispatch(HistoryIntent.Load, isIncome)
        viewModel.effect.collect { eff ->
            if (eff is HistoryEffect.ShowSnackbar) snackbarHostState.showSnackbar(eff.message)
        }
    }

    BaseScaffold(
        title = stringResource(R.string.my_history),
        action =
            TopBarAction(
                iconRes = R.drawable.analys,
                contentDescription = "Анализ",
                onClick = {
                    navigator.navigate(Route.AnalysisTransactions(isIncome = isIncome))
                },
            ),
        actionState = ActionState.Shown,
        backState = BackState.Shown,
        backAction = BackAction(onClick = navigator::navigateUp),
        fabState = FabState.Hidden,
    ) { innerPadding ->

        when {
            state.isLoading ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.TopCenter,
                ) { CircularProgressIndicator() }

            state.error != null && isOnline -> {
                BaseHistoryTopColumnPlaceholder(innerPadding, currency = currency, state.error)
            }

            else -> {
                if (state.list.isNotEmpty()) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                    ) {

                        if (!isOnline){
                            NoInternetBanner()

                            HorizontalDivider(
                                modifier = Modifier,
                                color = MaterialTheme.colorScheme.surfaceDim,
                                thickness = 1.dp,
                            )
                        }

                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Начало",
                            trailText = monthYear,
                        )
                        HorizontalDivider(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            thickness = 1.dp,
                        )
                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Конец",
                            trailText = nowWithTime,
                        )
                        HorizontalDivider(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            thickness = 1.dp,
                        )
                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Сумма",
                            trailText = "${
                                state.total.toString().toPrettyNumber()
                            } ${state.list[0].currency.toCurrencySymbol()}",
                        )

                        LazyColumn(
                            modifier =
                                Modifier
                                    .fillMaxSize(),
                        ) {
                            items(state.list) { historyItem ->
                                HistoryRow(
                                    modifier =
                                        Modifier.clickable {
                                            navigator.navigate(
                                                Route.AddOrEditTransaction(
                                                    transactionId = historyItem.id,
                                                    isIncome = isIncome,
                                                ),
                                            )
                                        },
                                    historyItem,
                                )

                                HorizontalDivider(
                                    modifier = Modifier,
                                    color = MaterialTheme.colorScheme.surfaceDim,
                                    thickness = 1.dp,
                                )
                            }
                        }
                    }
                } else {
                    BaseHistoryTopColumnPlaceholder(innerPadding, currency = currency)
                }
            }
        }

    }
}
