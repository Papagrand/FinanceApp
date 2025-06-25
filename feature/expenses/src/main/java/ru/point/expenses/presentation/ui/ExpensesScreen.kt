package ru.point.expenses.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.core.common.AccountPreferences
import ru.point.core.ui.ActionState
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.core.ui.NoInternetBanner
import ru.point.core.ui.TopBarAction
import ru.point.core.utils.NetworkHolder
import ru.point.data.repositoryImpl.TransactionRepositoryImpl
import ru.point.domain.model.TransactionPlaceHolder
import ru.point.domain.usecase.GetExpensesTodayUseCase
import ru.point.expenses.R
import ru.point.expenses.presentation.mvi.expenses.ExpensesEffect
import ru.point.expenses.presentation.mvi.expenses.ExpensesIntent
import ru.point.expenses.presentation.mvi.expenses.ExpensesViewModel
import ru.point.expenses.presentation.mvi.expenses.ExpensesViewModelFactory
import ru.point.expenses.presentation.ui.composableFunctions.ExpensesColumn
import ru.point.navigation.Navigator
import ru.point.navigation.Route
import ru.point.network.client.RetrofitProvider

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
    val context = LocalContext.current

    val prefs = remember { AccountPreferences(context) }

    val repo = TransactionRepositoryImpl(RetrofitProvider.instance)
    val useCase = GetExpensesTodayUseCase(repo)
    val factory = remember { ExpensesViewModelFactory(useCase, prefs) }
    val viewModel: ExpensesViewModel = viewModel(factory = factory)

    val state by viewModel.state.collectAsState()
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
                    navigator.navigate(Route.ExpensesHistory)
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
