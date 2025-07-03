package ru.point.transactions.expenses.ui.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.api.model.TransactionPlaceHolder
import ru.point.navigation.Navigator
import ru.point.navigation.Route
import ru.point.transactions.R
import ru.point.transactions.expenses.ui.composable.composableFunctions.ExpensesColumn
import ru.point.transactions.expenses.ui.mvi.ExpensesEffect
import ru.point.transactions.expenses.ui.mvi.ExpensesIntent
import ru.point.transactions.expenses.ui.mvi.ExpensesViewModel
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState
import ru.point.ui.composables.NoInternetBanner
import ru.point.ui.composables.TopBarAction
import ru.point.ui.di.LocalViewModelFactory
import ru.point.utils.network.NetworkHolder

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
fun ExpensesScreen(
    navigator: Navigator,
    onAddClick: () -> Unit = {},
) {
    val viewModel: ExpensesViewModel = viewModel(factory = LocalViewModelFactory.current)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val currency by viewModel.currency.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val tracker = remember { NetworkHolder.tracker }

    val placeholder = expensesPlaceholder(currency)

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
                    navigator.navigate(Route.History)
                },
            ),
        actionState = ActionState.Shown,
        fabState = FabState.Shown,
        onFabClick = onAddClick,
        snackbarHostState = snackbarHostState,
    ) { innerPadding ->

        NoInternetBanner(tracker = tracker)

        ExpensesColumn(innerPadding, state, placeholder)
    }
}

private fun expensesPlaceholder(currency: String): TransactionPlaceHolder {
    val placeholder =
        TransactionPlaceHolder(
            amount = "0",
            currency = currency,
        )
    return placeholder
}
