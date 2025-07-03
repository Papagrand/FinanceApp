package ru.point.account.ui.composable.accountEdit

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.account.R
import ru.point.account.ui.mvi.accountEdit.AccountEditEffect
import ru.point.account.ui.mvi.accountEdit.AccountEditIntent
import ru.point.account.ui.mvi.accountEdit.AccountEditViewModel
import ru.point.navigation.Navigator
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BackAction
import ru.point.ui.composables.BackState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState
import ru.point.ui.composables.NoInternetBanner
import ru.point.ui.composables.TopBarAction
import ru.point.ui.di.LocalViewModelFactory
import ru.point.utils.network.NetworkHolder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountEditScreen(
    navigator: Navigator,
    onAddClick: () -> Unit = {},
) {
    val viewModel: AccountEditViewModel = viewModel(factory = LocalViewModelFactory.current)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val tracker = remember { NetworkHolder.tracker }

    LaunchedEffect(Unit) {
        viewModel.dispatch(AccountEditIntent.Load)
        viewModel.effect.collect { effect ->
            when (effect) {
                is AccountEditEffect.ShowSnackbar ->
                    snackbarHostState.showSnackbar(effect.message)

                AccountEditEffect.Finish -> navigator.popBackStack()
            }
        }
    }

    BaseScaffold(
        title = stringResource(R.string.my_account),
        action =
            TopBarAction(
                iconRes = R.drawable.accept_edit,
                contentDescription = "Сохранить",
                onClick = {
                    viewModel.dispatch(AccountEditIntent.Save)
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
    ) { innerPadding ->

        when {
            state.isLoading -> {
            }

            state.error != null -> {
                Log.e("logError", state.error.toString())
            }

            else -> {
                AccountEditScreenBody(
                    state = state,
                    onNameChange = { viewModel.dispatch(AccountEditIntent.ChangeName(it)) },
                    onBalanceChange = { viewModel.dispatch(AccountEditIntent.ChangeBalance(it)) },
                    onCurrencyChange = { viewModel.dispatch(AccountEditIntent.ChangeCurrency(it)) },
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
        NoInternetBanner(tracker = tracker)
    }
}
