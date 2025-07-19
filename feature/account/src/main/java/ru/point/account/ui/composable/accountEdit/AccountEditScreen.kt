package ru.point.account.ui.composable.accountEdit

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import ru.point.account.R
import ru.point.account.di.component.DaggerAccountEditComponent
import ru.point.account.di.deps.AccountDepsStore
import ru.point.account.ui.mvi.accountEdit.AccountEditEffect
import ru.point.account.ui.mvi.accountEdit.AccountEditIntent
import ru.point.account.ui.mvi.accountEdit.AccountEditViewModel
import ru.point.navigation.Navigator
import ru.point.navigation.Route
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BackAction
import ru.point.ui.composables.BackState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState
import ru.point.ui.composables.TopBarAction
import ru.point.ui.di.LocalInternetTracker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountEditScreen(
    navigator: Navigator,
) {
    val accountEditComponent =
        remember {
            DaggerAccountEditComponent
                .builder()
                .deps(accountDeps = AccountDepsStore.accountDeps)
                .build()
        }

    val viewModel = viewModel<AccountEditViewModel>(factory = accountEditComponent.accountEditViewModelFactory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val isOnline by LocalInternetTracker.current.online.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.dispatch(AccountEditIntent.Load)
        viewModel.effect.collect { effect ->
            when (effect) {
                is AccountEditEffect.ShowSnackbar ->
                    snackbarHostState.showSnackbar(effect.message)

                AccountEditEffect.Finish -> navigator.navigate(Route.Account)
            }
        }
    }

    BaseScaffold(
        title = stringResource(R.string.edit_account),
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
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(top = 32.dp),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null && isOnline -> {
                Log.e("logError", state.error.toString())
            }

            else -> {
                AccountEditScreenBody(
                    state = state,
                    isOnline = isOnline,
                    onNameChange = { viewModel.dispatch(AccountEditIntent.ChangeName(it)) },
                    onBalanceChange = { viewModel.dispatch(AccountEditIntent.ChangeBalance(it)) },
                    onCurrencyChange = { viewModel.dispatch(AccountEditIntent.ChangeCurrency(it)) },
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}
