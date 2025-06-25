package ru.point.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.point.core.utils.toCurrencySymbol
import ru.point.core.utils.toPrettyNumber
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale
import ru.point.core.R

@Composable
fun BaseHistoryTopColumnPlaceholder(
    innerPadding: PaddingValues,
    error: String? = null,
) {

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        BaseHistoryTopElement(
            modifier = Modifier,
            contentText = stringResource(R.string.start_period),
            trailText = monthYear
        )
        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp
        )
        BaseHistoryTopElement(
            modifier = Modifier,
            contentText = stringResource(R.string.end_period),
            trailText = nowWithTime
        )
        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp
        )
        BaseHistoryTopElement(
            modifier = Modifier,
            contentText = stringResource(R.string.amount_placeholder),
            trailText = "0 ₽"
        )
        if (error != null){
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = error,
            )
        }
    }
}

@Composable
fun BaseHistoryTopElement(
    modifier: Modifier,
    contentText: String,
    trailText: String,
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
                    text = contentText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trail = {
                Text(
                    trailText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}