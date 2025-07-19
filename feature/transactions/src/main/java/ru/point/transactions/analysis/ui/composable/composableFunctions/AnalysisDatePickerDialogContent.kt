package ru.point.transactions.analysis.ui.composable.composableFunctions

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnalysisDatePickerDialogContent(
    pickerState: DatePickerState,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val formatter  = remember { DateTimeFormatter.ISO_DATE }
    val pickerColors =
        DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
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
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                pickerState.selectedDateMillis?.let { millis ->
                    val iso = Instant.ofEpochMilli(millis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(formatter)
                    onConfirm(iso)
                }
            }) { Text("Oк") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        },
        colors = pickerColors,
    ) {
        DatePicker(
            state = pickerState,
            colors = pickerColors,
            headline = null,
            title = null,
            showModeToggle = false,
        )
    }

}