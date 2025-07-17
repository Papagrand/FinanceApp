package ru.point.transactions.analysis.ui.composable.composableFunctions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.ZoneId
import ru.point.transactions.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnalysisDatePicker(
    modifier: Modifier = Modifier,
    startIso: String,
    endIso: String,
    onStartChanged: (String) -> Unit,
    onEndChanged: (String) -> Unit,
) {
    var startPickerOpen by remember { mutableStateOf(false) }
    var endPickerOpen by remember { mutableStateOf(false) }

    Column(modifier) {
        MonthPickerCard(
            contentTextResId = R.string.period_start,
            date = startIso,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = { startPickerOpen = true }
        )

        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )

        MonthPickerCard(
            contentTextResId = R.string.period_end,
            date = endIso,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = { endPickerOpen = true }
        )
    }

    if (startPickerOpen) {
        val todayMillis = remember { System.currentTimeMillis() }
        val endMillis = remember(endIso) { LocalDate.parse(endIso).toEpochMilli() }
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = LocalDate.parse(startIso).toEpochMilli(),
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                        utcTimeMillis <= endMillis && utcTimeMillis <= todayMillis
                }
            )

        AnalysisDatePickerDialogContent(
            pickerState = datePickerState,
            onConfirm = { onStartChanged(it); startPickerOpen = false },
            onDismiss = { startPickerOpen = false }
        )
    }

    if (endPickerOpen) {
        val todayMillis = remember { System.currentTimeMillis() }
        val startMillis = remember(startIso) { LocalDate.parse(startIso).toEpochMilli() }
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = LocalDate.parse(endIso).toEpochMilli(),
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                        utcTimeMillis >= startMillis && utcTimeMillis <= todayMillis
                }
            )

        AnalysisDatePickerDialogContent(
            pickerState = datePickerState,
            onConfirm = { onEndChanged(it); endPickerOpen = false },
            onDismiss = { endPickerOpen = false }
        )
    }
}

private fun LocalDate.toEpochMilli(): Long =
    atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()