package ru.point.expenses.presentation.ui.composableFunctions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.point.core.ui.BaseListItem
import ru.point.expenses.R

@Composable
fun TotalExpensesToday(
    modifier: Modifier,
    total: String,
) {
    Surface(
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        BaseListItem(
            onClick = { },
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            rowHeight = 56.dp,
            lead = { },
            content = {
                Text(
                    text = stringResource(R.string.total),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            trail = {
                Text(
                    total,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
        )
    }
}
