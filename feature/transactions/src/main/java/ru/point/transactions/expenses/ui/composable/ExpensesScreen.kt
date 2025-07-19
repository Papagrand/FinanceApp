package ru.point.transactions.expenses.ui.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.navigation.Navigator
import ru.point.navigation.Route
import ru.point.transactions.R
import ru.point.transactions.di.TransactionDepsStore
import ru.point.transactions.expenses.di.component.DaggerExpensesComponent
import ru.point.transactions.expenses.ui.composable.composableFunctions.ExpensesColumn
import ru.point.transactions.expenses.ui.mvi.ExpensesEffect
import ru.point.transactions.expenses.ui.mvi.ExpensesIntent
import ru.point.transactions.expenses.ui.mvi.ExpensesViewModel
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState
import ru.point.ui.composables.TopBarAction
import ru.point.ui.di.LocalInternetTracker

/**
 * ExpensesScreen
 *
 * Ответственность:
 * - отображение экрана с расходами за сегодня: шапка, кнопка истории, список транзакций или индикатор загрузки/ошибки;
 * - отправка MVI-интентов (Load, Retry) и обработка эффектов (показывать Snackbar);
 * - навигация к истории расходов и реакция на нажатие FAB;
 * - показ баннера при отсутствии соединения.
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(navigator: Navigator) {
    val expensesComponent =
        remember {
            DaggerExpensesComponent
                .builder()
                .deps(transactionDeps = TransactionDepsStore.transactionDeps)
                .build()
        }

    val viewModel = viewModel<ExpensesViewModel>(factory = expensesComponent.expensesViewModelFactory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val isOnline by LocalInternetTracker.current.online.collectAsState()

    val currency by viewModel.currency.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.dispatch(ExpensesIntent.Load)
        viewModel.effect.collect { eff ->
            if (eff is ExpensesEffect.ShowSnackbar) {
                snackbarHostState.showSnackbar(eff.message)
            }
        }
    }

    BaseScaffold(
        title = stringResource(R.string.expenses_today),
        action =
            TopBarAction(
                iconRes = R.drawable.history,
                contentDescription = "История",
                onClick = {
                    navigator.navigate(Route.History(isIncome = false))
                },
            ),
        actionState = ActionState.Shown,
        fabState = FabState.Shown,
        onFabClick = {
            navigator.navigate(
                Route.AddOrEditTransaction(
                    isIncome = false,
                ),
            )
        },
        snackbarHostState = snackbarHostState,
    ) { innerPadding ->

        ExpensesColumn(
            isOnline = isOnline,
            innerPadding,
            state,
            currency,
            onItemClick = { transactionId ->
                navigator.navigate(
                    Route.AddOrEditTransaction(
                        transactionId = transactionId,
                        isIncome = false,
                    ),
                )
            },
        )
    }
}
