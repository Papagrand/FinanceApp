package ru.point.income.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import ru.point.core.ui.BackAction
import ru.point.core.ui.BackState
import ru.point.core.ui.BaseHistoryTopElement
import ru.point.core.ui.BaseListItem
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.core.ui.NoInternetBanner
import ru.point.core.ui.TopBarAction
import ru.point.core.utils.NetworkHolder
import ru.point.core.utils.toCurrencySymbol
import ru.point.core.utils.toPrettyNumber
import ru.point.data.repositoryImpl.TransactionRepositoryImpl
import ru.point.domain.model.Transaction
import ru.point.domain.usecase.GetTransactionHistoryUseCase
import ru.point.income.R
import ru.point.income.presentation.mvi.incomesHistory.IncomesHistoryEffect
import ru.point.income.presentation.mvi.incomesHistory.IncomesHistoryIntent
import ru.point.income.presentation.mvi.incomesHistory.IncomesHistoryViewModel
import ru.point.income.presentation.mvi.incomesHistory.IncomesHistoryViewModelFactory
import ru.point.navigation.Navigator
import ru.point.network.BuildConfig
import ru.point.network.client.RetrofitProvider
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomesHistoryScreen(
    navigator: Navigator,
    onAddClick: () -> Unit = {}
) {
    val repo = TransactionRepositoryImpl(RetrofitProvider.instance)
    val useCase = GetTransactionHistoryUseCase(repo, true)
    val factory = remember { IncomesHistoryViewModelFactory(useCase) }
    val viewModel: IncomesHistoryViewModel = viewModel(factory = factory)

    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val tracker = remember { NetworkHolder.tracker }

    val monthYear = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru")))
        .replaceFirstChar { it.uppercase() }

    val nowWithTime = LocalDateTime.now()
        .format(
            DateTimeFormatter.ofPattern(
                "dd.MM.yyyy HH:mm",
                Locale("ru")
            )
        )

    LaunchedEffect(Unit) {
        viewModel.dispatch(IncomesHistoryIntent.Load(BuildConfig.ACCOUNT_ID.toInt())) //Todo пока без кеша, определяю в local.properties
        viewModel.effect.collect { eff ->
            if (eff is IncomesHistoryEffect.ShowSnackbar) snackbarHostState.showSnackbar(eff.message)
        }
    }

    BaseScaffold(
        title = stringResource(R.string.my_history),
        action = TopBarAction(
            iconRes = R.drawable.analys,
            contentDescription = "Анализ",
            onClick = {}
        ),
        actionState = ActionState.Shown,
        backState = BackState.Shown,
        backAction = BackAction(onClick = navigator::popBackStack),
        fabState = FabState.Hidden,
    ) { innerPadding ->

        NoInternetBanner(tracker = tracker)

        when {
            state.isLoading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) { CircularProgressIndicator() }

            state.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    BaseHistoryTopElement(
                        modifier = Modifier,
                        contentText = "Начало",
                        trailText = monthYear
                    )
                    HorizontalDivider(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        thickness = 1.dp
                    )
                    BaseHistoryTopElement(
                        modifier = Modifier,
                        contentText = "Конец",
                        trailText = nowWithTime
                    )
                    HorizontalDivider(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        thickness = 1.dp
                    )
                    BaseHistoryTopElement(
                        modifier = Modifier,
                        contentText = "Сумма",
                        trailText = "0 ₽"
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Начало",
                            trailText = monthYear
                        )
                        HorizontalDivider(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            thickness = 1.dp
                        )
                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Конец",
                            trailText = nowWithTime
                        )
                        HorizontalDivider(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            thickness = 1.dp
                        )
                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Сумма",
                            trailText = "${
                                state.total.toString().toPrettyNumber()
                            } ${state.list[0].currency.toCurrencySymbol()}"
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(state.list) { incomeHistoryItem ->
                                IncomesHistoryRow(
                                    modifier = Modifier,
                                    incomeHistoryItem
                                )

                                HorizontalDivider(
                                    modifier = Modifier,
                                    color = MaterialTheme.colorScheme.surfaceDim,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Начало",
                            trailText = monthYear
                        )
                        HorizontalDivider(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            thickness = 1.dp
                        )
                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Конец",
                            trailText = nowWithTime
                        )
                        HorizontalDivider(
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.surfaceDim,
                            thickness = 1.dp
                        )
                        BaseHistoryTopElement(
                            modifier = Modifier,
                            contentText = "Сумма",
                            trailText = "0 ₽"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IncomesHistoryRow(
    modifier: Modifier,
    incomeHistoryItem: Transaction,
    onClick: () -> Unit = {}
) = BaseListItem(
    rowHeight = 70.dp,
    onClick = onClick,
    modifier = modifier
        .fillMaxWidth()
        .clickable(onClick = { })
        .padding(horizontal = 16.dp),
    lead = {
        val initials =
            remember(incomeHistoryItem.categoryName) { initialsOf(incomeHistoryItem.categoryName) }
        val iconText = incomeHistoryItem.emoji ?: initials

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iconText,
                style = if (incomeHistoryItem.emoji != null)
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
            text = incomeHistoryItem.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (incomeHistoryItem.comment != "") {
            Text(
                text = incomeHistoryItem.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    },

    trail = {
        val instant = Instant.parse(incomeHistoryItem.dateTime)

        val dateTime = instant
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        val incomeTime = dateTime.format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale("ru"))
        )

        Column (
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${incomeHistoryItem.amount.toPrettyNumber()} ${incomeHistoryItem.currency.toCurrencySymbol()}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = incomeTime,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.right_arrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )

    }
)


private fun initialsOf(title: String): String {
    val words = title.trim().split("\\s+".toRegex())
    val first = words.getOrNull(0)?.firstOrNull()?.uppercase() ?: ""
    val second = words.getOrNull(1)?.firstOrNull()?.uppercase() ?: ""
    return buildString {
        append(first)
        if (second.isNotEmpty()) append(second)
    }
}