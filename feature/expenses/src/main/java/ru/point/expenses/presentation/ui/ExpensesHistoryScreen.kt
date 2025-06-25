package ru.point.expenses.presentation.ui

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.core.common.AccountPreferences
import ru.point.core.ui.ActionState
import ru.point.core.ui.BackAction
import ru.point.core.ui.BackState
import ru.point.core.ui.BaseHistoryTopColumnPlaceholder
import ru.point.core.ui.BaseHistoryTopElement
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.core.ui.NoInternetBanner
import ru.point.core.ui.TopBarAction
import ru.point.core.utils.NetworkHolder
import ru.point.core.utils.toCurrencySymbol
import ru.point.core.utils.toPrettyNumber
import ru.point.data.repositoryImpl.TransactionRepositoryImpl
import ru.point.domain.usecase.GetTransactionHistoryUseCase
import ru.point.expenses.R
import ru.point.expenses.presentation.mvi.expensesHistory.ExpensesHistoryEffect
import ru.point.expenses.presentation.mvi.expensesHistory.ExpensesHistoryIntent
import ru.point.expenses.presentation.mvi.expensesHistory.ExpensesHistoryViewModel
import ru.point.expenses.presentation.mvi.expensesHistory.ExpensesHistoryViewModelFactory
import ru.point.expenses.presentation.ui.composable_functions.ExpensesHistoryRow
import ru.point.navigation.Navigator
import ru.point.network.client.RetrofitProvider
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * ExpensesHistoryScreen
 *
 * Ответственность:
 * - отображение истории расходов за текущий период: шапка с датами и суммой, список транзакций или состояния загрузки/ошибки;
 * - отправка MVI-интента Load и обработка эффектов (показ Snackbar);
 * - навигация назад и реакция на нажатие кнопки анализа;
 * - показ баннера об отсутствии подключения.
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesHistoryScreen(
    navigator: Navigator,
    onAddClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val prefs = remember { AccountPreferences(context) }

    val repo = TransactionRepositoryImpl(RetrofitProvider.instance)
    val useCase = GetTransactionHistoryUseCase(repo, false)
    val factory = remember { ExpensesHistoryViewModelFactory(useCase, prefs) }
    val viewModel: ExpensesHistoryViewModel = viewModel(factory = factory)

    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val tracker = remember { NetworkHolder.tracker }

    val monthYear = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru")))
        .replaceFirstChar { it.uppercase() }

    val nowWithTime = LocalDateTime.now()
        .format(
            DateTimeFormatter.ofPattern(
                "dd.MM.yyyy HH:mm",
                Locale("ru")
            )
        )

    LaunchedEffect(Unit) {
        viewModel.dispatch(ExpensesHistoryIntent.Load)
        viewModel.effect.collect { eff ->
            if (eff is ExpensesHistoryEffect.ShowSnackbar) snackbarHostState.showSnackbar(eff.message)
        }
    }

    BaseScaffold(
        title = stringResource(R.string.my_history),
        action = TopBarAction(
            iconRes = R.drawable.analys,
            contentDescription = "Анализ",
            onClick = {}
        ),
        actionState = ActionState.Shown,
        backState = BackState.Shown,
        backAction = BackAction(onClick = navigator::popBackStack),
        fabState = FabState.Hidden,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->

        NoInternetBanner(tracker = tracker)

        when {
            state.isLoading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) { CircularProgressIndicator() }

            state.error != null -> {
                BaseHistoryTopColumnPlaceholder(innerPadding, state.error)
            }

            else -> {
                if (state.list.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Начало",
                            trailText = monthYear
                        )
                        HorizontalDivider(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            thickness = 1.dp
                        )
                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Конец",
                            trailText = nowWithTime
                        )
                        HorizontalDivider(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            thickness = 1.dp
                        )
                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Сумма",
                            trailText = "${
                                state.total.toString().toPrettyNumber()
                            } ${state.list[0].currency.toCurrencySymbol()}"
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(state.list) { expenseHistoryItem ->
                                ExpensesHistoryRow(
                                    modifier = Modifier,
                                    expenseHistoryItem
                                )

                                HorizontalDivider(
                                    modifier = Modifier,
                                    color = MaterialTheme.colorScheme.surfaceDim,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                } else {
                    BaseHistoryTopColumnPlaceholder(innerPadding)
                }
            }
        }
    }
}
