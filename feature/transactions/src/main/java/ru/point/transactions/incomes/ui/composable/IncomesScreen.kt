package ru.point.transactions.incomes.ui.composable

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
import ru.point.transactions.incomes.di.DaggerIncomesComponent
import ru.point.transactions.incomes.ui.composable.composableFunctions.IncomesColumn
import ru.point.transactions.incomes.ui.mvi.IncomesEffect
import ru.point.transactions.incomes.ui.mvi.IncomesIntent
import ru.point.transactions.incomes.ui.mvi.IncomesViewModel
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState
import ru.point.ui.composables.NoInternetBanner
import ru.point.ui.composables.TopBarAction
import ru.point.ui.di.LocalInternetTracker

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
    val incomesComponent =
        remember {
            DaggerIncomesComponent
                .builder()
                .deps(transactionDeps = TransactionDepsStore.transactionDeps)
                .build()
        }

    val viewModel = viewModel<IncomesViewModel>(factory = incomesComponent.incomesViewModelFactory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val isOnline by LocalInternetTracker.current.online.collectAsState()

    val currency by viewModel.currency.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

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

        if (!isOnline) {
            NoInternetBanner()
        }

        IncomesColumn(innerPadding, state, currency)
    }
}
