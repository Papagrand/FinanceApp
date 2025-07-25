package ru.point.account.ui.composable.account

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.account.R
import ru.point.account.di.component.DaggerAccountComponent
import ru.point.account.di.deps.AccountDepsStore
import ru.point.account.ui.mvi.account.AccountEffect
import ru.point.account.ui.mvi.account.AccountIntent
import ru.point.account.ui.mvi.account.AccountViewModel
import ru.point.api.model.AccountDto
import ru.point.chart.ui.ChartBarGraph
import ru.point.navigation.Navigator
import ru.point.navigation.Route
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState
import ru.point.ui.composables.NoInternetBanner
import ru.point.ui.composables.TopBarAction
import ru.point.ui.di.LocalInternetTracker
import ru.point.utils.extensionsAndParsers.toCurrencySymbol

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
) {
    val accountComponent =
        remember {
            DaggerAccountComponent
                .builder()
                .deps(accountDeps = AccountDepsStore.accountDeps)
                .build()
        }

    val viewModel = viewModel<AccountViewModel>(factory = accountComponent.accountViewModelFactory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val isOnline by LocalInternetTracker.current.online.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.dispatch(AccountIntent.Load)
        viewModel.dispatch(AccountIntent.RefreshChart)
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
                onClick = {
                    navigator.navigate(Route.AccountEdit)
                },
            ),
        actionState = ActionState.Shown,
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
                    if (!isOnline) {
                        NoInternetBanner()

                        HorizontalDivider(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            thickness = 1.dp,
                        )
                    }
                    if (state.accountData != null) {
                        Balance(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                            state.accountData!!,
                        )
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
                            currency = state.accountData!!.currency.toCurrencySymbol(),
                        )
                    } else {
                        Balance(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                            accountPlaceholder(),
                        )
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
                            currency = "$",
                        )
                    }
                    
                    HorizontalDivider(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        thickness = 1.dp,
                    )
                    ChartBarGraph(
                        entries = state.chart,
                        modifier = Modifier
                            .padding(top = 56.dp, end = 10.dp)
                            .fillMaxWidth()
                            .height(220.dp)
                    )

                }
            }
        }
    }

}

private fun accountPlaceholder(): AccountDto {
    val placeholder =
        AccountDto(
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
