package ru.point.ui.composables

import androidx.compose.foundation.clickable
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
    modifier: Modifier,
    rowHeight: Dp = 70.dp,
    onClick: (() -> Unit)? = null,
    lead: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
    trail: @Composable RowScope.() -> Unit = {},
) {
    val rowModifier =
        if (onClick != null) {
            modifier.height(rowHeight).clickable { onClick() }
        } else {
            modifier.height(rowHeight)
        }

    Row(
        modifier = rowModifier,
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
