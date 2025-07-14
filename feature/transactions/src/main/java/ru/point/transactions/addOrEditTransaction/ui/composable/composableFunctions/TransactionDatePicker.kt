package ru.point.transactions.addOrEditTransaction.ui.composable.composableFunctions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.point.ui.composables.SimpleListRow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionDatePicker(
    modifier: Modifier = Modifier,
    title: String,
    currentOrEmptyDate: String,
    onDateChange: (String) -> Unit,
) {
    val backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer
    val formatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    val defaultDate by remember {
        mutableStateOf(LocalDate.now().format(formatter))
    }
    val initialMillis =
        remember(currentOrEmptyDate) {
            runCatching {
                LocalDate.parse(currentOrEmptyDate, formatter)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            }.getOrNull()
        }

    var showDialog by remember { mutableStateOf(false) }

    val lastDate = currentOrEmptyDate.ifBlank { defaultDate }

    onDateChange(lastDate)

    SimpleListRow(
        modifier =
            modifier
                .clickable { showDialog = true }
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        rowHeight = 70.dp,
        content = { Text(title) },
        trail = {
            Text(
                text = lastDate,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
    )

    if (showDialog) {
        val todayMillis = remember { System.currentTimeMillis() }
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = initialMillis ?: todayMillis,
                selectableDates =
                    object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= todayMillis
                    },
            )
        val pickerColors =
            DatePickerDefaults.colors(
                containerColor = backgroundColor,
                titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                headlineContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                dividerColor = MaterialTheme.colorScheme.primary,
                selectedDayContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedDayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                todayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                todayDateBorderColor = MaterialTheme.colorScheme.primaryContainer,
            )

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val localDate =
                                Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            onDateChange(localDate.format(formatter))
                        }
                        showDialog = false
                    },
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Отмена") }
            },
            colors = pickerColors,
        ) {
            DatePicker(
                state = datePickerState,
                colors = pickerColors,
                headline = null,
                title = null,
                showModeToggle = false,
            )
        }
    }
}
