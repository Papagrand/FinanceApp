package ru.point.transactions.analysis.ui.composable.composableFunctions

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import ru.point.transactions.R
import ru.point.ui.composables.BaseListItem

@Composable
internal fun MonthPickerCard(
    @StringRes contentTextResId: Int,
    date: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {

    BaseListItem(
        modifier = modifier,
        content = {
            Text(
                text = stringResource(contentTextResId)
            )
        },
        trail = {
            Box(
                modifier = Modifier
                    .height(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .clip(RoundedCornerShape(100.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = onClick
                    )
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dateParser(date),
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    )
}

private fun dateParser(isoString: String): String {
    val date = LocalDate.parse(isoString)
    return date.format(
        DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    ).replaceFirstChar { it.uppercase() }
}


@Preview(showBackground = true)
@Composable
private fun MonthPickerCardPreview() {
    MonthPickerCard(
        contentTextResId = R.string.period_start,
        date = "февраль 2025",
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}