package ru.point.transactions.addOrEditTransaction.ui.composable.composableFunctions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.point.ui.composables.SimpleListRow
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionTimePicker(
    modifier: Modifier = Modifier,
    currentOrEmptyTime: String,
    onTimeChange: (parsedTime: String) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    val defaultTime =
        remember {
            LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        }

    onTimeChange(defaultTime)

    val title = "Время"

    SimpleListRow(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { showDialog = true }
                .padding(horizontal = 16.dp),
        rowHeight = 70.dp,
        content = { Text(title) },
        trail = { Text(currentOrEmptyTime.ifBlank { defaultTime }) },
    )

    if (showDialog) {
        val calendar = remember { Calendar.getInstance() }
        val (initHour, initMinute) =
            remember(currentOrEmptyTime) {
                currentOrEmptyTime
                    .split(":")
                    .runCatching {
                        Pair(
                            getOrNull(0)?.toIntOrNull() ?: calendar.get(Calendar.HOUR_OF_DAY),
                            getOrNull(1)?.toIntOrNull() ?: calendar.get(Calendar.MINUTE),
                        )
                    }
                    .getOrDefault(
                        Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)),
                    )
            }

        val timePickerState =
            rememberTimePickerState(
                initialHour = initHour,
                initialMinute = initMinute,
                is24Hour = true,
            )

        val clockColors =
            TimePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                selectorColor = MaterialTheme.colorScheme.primaryContainer,
                clockDialColor = MaterialTheme.colorScheme.background,
                clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                periodSelectorBorderColor = MaterialTheme.colorScheme.primary,
                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.background,
                timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onSurface,
            )

        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            title = {},
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeChange(
                            "%02d:%02d".format(timePickerState.hour, timePickerState.minute),
                        )
                        showDialog = false
                    },
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Отмена") }
            },
            text = {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                    TimePicker(
                        state = timePickerState,
                        colors = clockColors,
                    )
                }
            },
        )
    }
}
