package ru.point.expenses.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.core.ui.ActionState
import ru.point.core.ui.BaseListItem
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.core.ui.TopBarAction
import ru.point.domain.model.Transaction
import ru.point.expenses.R
import ru.point.domain.usecase.GetExpensesTodayUseCase
import ru.point.expenses.presentation.mvi.expenses.ExpensesViewModel
import ru.point.expenses.presentation.mvi.expenses.ExpensesViewModelFactory
import ru.point.network.client.RetrofitProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import ru.point.core.common.AccountPreferences
import ru.point.core.ui.NoInternetBanner
import ru.point.core.utils.NetworkHolder
import ru.point.expenses.presentation.mvi.expenses.ExpensesEffect
import ru.point.expenses.presentation.mvi.expenses.ExpensesIntent
import ru.point.core.utils.toPrettyNumber
import ru.point.core.utils.toCurrencySymbol
import ru.point.data.repositoryImpl.TransactionRepositoryImpl
import ru.point.domain.model.TransactionPlaceHolder
import ru.point.navigation.Navigator
import ru.point.navigation.Route


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    navigator: Navigator,
    onAddClick: () -> Unit = {}
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
            if (eff is ExpensesEffect.ShowSnackbar){
                snackbarHostState.showSnackbar(eff.message)
            }
        }
    }

    BaseScaffold(
        title = stringResource(R.string.expenses_today),
        action = TopBarAction(
            iconRes = R.drawable.history,
            contentDescription = "История",
            onClick = {
                navigator.navigate(Route.ExpensesHistory)
            }
        ),
        actionState = ActionState.Shown,
        fabState = FabState.Shown,
        onFabClick = onAddClick,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->

        NoInternetBanner(tracker = tracker)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.isLoading -> Box(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.TopCenter
                ) { CircularProgressIndicator() }

                state.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),

                        ) {
                        TotalExpensesToday(
                            modifier = Modifier,
                            total = "${placeholder.amount.toPrettyNumber()} ${placeholder.currency.toCurrencySymbol()}"
                        )
                        HorizontalDivider(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            thickness = 1.dp
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            text = "${state.error}",
                        )
                    }

                }

                else -> {
                    if (state.list.isNotEmpty()) {
                        TotalExpensesToday(
                            modifier = Modifier,
                            total = "${state.total.toString().toPrettyNumber()} ${state.list[0].currency.toCurrencySymbol()}"
                        )
                    } else {
                        TotalExpensesToday(
                            modifier = Modifier,
                            total = "${placeholder.amount.toPrettyNumber()} ${placeholder.currency.toCurrencySymbol()}"
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        thickness = 1.dp
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(state.list) { expense ->
                            ExpenseRow(
                                modifier = Modifier,
                                expense
                            )
                            HorizontalDivider(
                                modifier = Modifier,
                                color = MaterialTheme.colorScheme.surfaceDim,
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }

        }
    }
}


@Composable
fun TotalExpensesToday(
    modifier: Modifier,
    total: String
) {
    Surface(
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BaseListItem(
            onClick = { },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            rowHeight = 56.dp,
            lead = { },
            content = {
                Text(
                    text = stringResource(R.string.total),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trail = {
                Text(
                    total,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}

@Composable
fun ExpenseRow(
    modifier: Modifier,
    expense: Transaction,
    onClick: () -> Unit = {}
) = BaseListItem(
    rowHeight = 70.dp,
    onClick = onClick,
    modifier = modifier
        .fillMaxWidth()
        .clickable(onClick = { })
        .padding(horizontal = 16.dp),
    lead = {
        val initials = remember(expense.categoryName) { initialsOf(expense.categoryName) }
        val iconText = expense.emoji ?: initials

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iconText,
                style = if (expense.emoji != null)
                    MaterialTheme.typography.bodyLarge
                else
                    TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.0.sp
                    )
            )
        }
    },

    content = {
        Text(
            text = expense.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (expense.comment != "") {
            Text(
                text = expense.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    },

    trail = {
        Text(
            text = "${expense.amount.toPrettyNumber()} ${expense.currency.toCurrencySymbol()}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.right_arrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
)

private fun expensesPlaceholder(): TransactionPlaceHolder {
    val placeholder: TransactionPlaceHolder = TransactionPlaceHolder(
        amount = "0",
        currency = "RUB"
    )
    return placeholder
}

private fun initialsOf(title: String): String {
    val words = title.trim().split("\\s+".toRegex())
    val first = words.getOrNull(0)?.firstOrNull()?.uppercase() ?: ""
    val second = words.getOrNull(1)?.firstOrNull()?.uppercase() ?: ""
    return buildString {
        append(first)
        if (second.isNotEmpty()) append(second)
    }
}