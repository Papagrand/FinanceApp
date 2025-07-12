package ru.point.transactions.addOrEditTransaction.ui.composable

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
import ru.point.transactions.R
import ru.point.transactions.addOrEditTransaction.di.DaggerAddOrEditTransactionComponent
import ru.point.transactions.addOrEditTransaction.ui.composable.composableFunctions.AddOrEditElementsColumn
import ru.point.transactions.addOrEditTransaction.ui.mvi.AddOrEditTransactionEffect
import ru.point.transactions.addOrEditTransaction.ui.mvi.AddOrEditTransactionIntent
import ru.point.transactions.addOrEditTransaction.ui.mvi.AddOrEditTransactionViewModel
import ru.point.transactions.di.TransactionDepsStore
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BackAction
import ru.point.ui.composables.BackState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState
import ru.point.ui.composables.NoInternetBanner
import ru.point.ui.composables.TopBarAction
import ru.point.ui.di.LocalInternetTracker

@Composable
fun AddOrEditTransactionScreen(
    navigator: Navigator,
    transactionId: Int,
    isIncome: Boolean,
) {
    val addOrEditTransactionComponent =
        remember {
            DaggerAddOrEditTransactionComponent
                .builder()
                .deps(transactionDeps = TransactionDepsStore.transactionDeps)
                .transactionId(transactionId.takeIf { it >= 0 })
                .isIncome(isIncome)
                .build()
        }

    val viewModel =
        viewModel<AddOrEditTransactionViewModel>(factory = addOrEditTransactionComponent.addOrEditTransactionViewModelFactory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val isOnline by LocalInternetTracker.current.online.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.dispatch(AddOrEditTransactionIntent.Load)
        viewModel.effect.collect { eff ->
            when (eff) {
                is AddOrEditTransactionEffect.ShowSnackbar ->
                    snackbarHostState.showSnackbar(eff.message)

                AddOrEditTransactionEffect.Finish -> navigator.popBackStack()
            }
        }
    }

    val onDeleteClick: () -> Unit = {
        viewModel.dispatch(AddOrEditTransactionIntent.DeleteTransaction)
    }

    BaseScaffold(
        title =
            if (isIncome) {
                stringResource(R.string.my_incomes)
            } else {
                stringResource(R.string.my_expenses)
            },
        action =
            TopBarAction(
                iconRes = R.drawable.accept_edit,
                contentDescription = "Сохранить",
                onClick = {
                    viewModel.dispatch(AddOrEditTransactionIntent.Save)
                },
            ),
        actionState = ActionState.Shown,
        backState = BackState.Shown,
        backAction =
            BackAction(
                iconRes = R.drawable.disline_edit,
                contentDescription = "Закрыть без сохранения",
                onClick = { navigator.popBackStack() },
            ),
        fabState = FabState.Hidden,
        snackbarHostState = snackbarHostState,
    ) { innerPadding ->
        if (!isOnline) {
            NoInternetBanner()
        }
        AddOrEditElementsColumn(
            innerPadding = innerPadding,
            onCommentChange = { viewModel.dispatch(AddOrEditTransactionIntent.CommentChanged(it)) },
            onDeleteClick = onDeleteClick,
            onIntent = viewModel::dispatch,
            state = state,
        )
    }
}
