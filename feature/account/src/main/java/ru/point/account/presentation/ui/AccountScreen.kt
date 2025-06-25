package ru.point.account.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.account.R
import ru.point.account.domain.model.Account
import ru.point.account.presentation.mvi.AccountEffect
import ru.point.account.presentation.mvi.AccountIntent
import ru.point.account.presentation.mvi.AccountViewModel
import ru.point.core.di.LocalViewModelFactory
import ru.point.core.ui.ActionState
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.core.ui.NoInternetBanner
import ru.point.core.ui.TopBarAction
import ru.point.core.utils.NetworkHolder
import ru.point.navigation.Navigator

/**
 * AccountScreen
 *
 * Ответственность:
 * - отображение экрана с балансом и валютой пользователя;
 * - обработка состояний загрузки, ошибки и успешного получения данных;
 * - реакция на события UI (добавление, показ снэкбаров, проверка сети).
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navigator: Navigator,
    onAddClick: () -> Unit = {},
) {
    val viewModel: AccountViewModel = viewModel(factory = LocalViewModelFactory.current)

    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val tracker = remember { NetworkHolder.tracker }

    LaunchedEffect(Unit) {
        viewModel.dispatch(AccountIntent.Load)
        viewModel.effect.collect { effect ->
            when (effect) {
                is AccountEffect.ShowSnackbar ->
                    snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    BaseScaffold(
        title = stringResource(R.string.my_account),
        action =
            TopBarAction(
                iconRes = R.drawable.edit,
                contentDescription = "Редактировать",
                onClick = {},
            ),
        actionState = ActionState.Shown,
        fabState = FabState.Shown,
        onFabClick = onAddClick,
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

            state.error != null -> {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                ) {
                    Balance(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                        accountPlaceholder(),
                    )
                    Text(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        text = "${state.error}",
                    )
                }
            }

            else -> {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                ) {
                    if (state.accountData != null) {
                        Balance(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                            state.accountData!!,
                        )
                    } else {
                        Balance(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                            accountPlaceholder(),
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        thickness = 1.dp,
                    )

                    Currency(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                    )
                }
            }
        }
        NoInternetBanner(tracker = tracker)
    }
}

private fun accountPlaceholder(): Account {
    val placeholder =
        Account(
            balance = "0",
            currency = "RUB",
            id = 1,
            userId = 1,
            name = "Баланс",
            createdAt = "",
            updatedAt = "",
        )
    return placeholder
}
