package ru.point.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SimpleListRow(
    modifier: Modifier = Modifier,
    rowHeight: Dp = 70.dp,
    lead: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
    trail: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier.height(rowHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        lead()
        Column(Modifier.weight(1f)) { content() }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            trail()
        }
    }
}
