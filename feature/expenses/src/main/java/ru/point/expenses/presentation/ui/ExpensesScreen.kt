package ru.point.expenses.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.core.di.LocalViewModelFactory
import ru.point.core.ui.ActionState
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.core.ui.NoInternetBanner
import ru.point.core.ui.TopBarAction
import ru.point.core.utils.NetworkHolder
import ru.point.domain.model.TransactionPlaceHolder
import ru.point.expenses.R
import ru.point.expenses.presentation.mvi.ExpensesEffect
import ru.point.expenses.presentation.mvi.ExpensesIntent
import ru.point.expenses.presentation.mvi.ExpensesViewModel
import ru.point.expenses.presentation.ui.composableFunctions.ExpensesColumn
import ru.point.navigation.Navigator
import ru.point.navigation.Route

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
    val snackbarHostState = remember { SnackbarHostState() }

    val tracker = remember { NetworkHolder.tracker }

    val placeholder = expensesPlaceholder()

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

private fun expensesPlaceholder(): TransactionPlaceHolder {
    val placeholder =
        TransactionPlaceHolder(
            amount = "0",
            currency = "RUB",
        )
    return placeholder
}
