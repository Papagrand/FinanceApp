package ru.point.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.point.core.ui.BaseListItem
import ru.point.settings.R

@Composable
fun SettingsRow(
    title: String,
    isTheme: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    var checked by remember { mutableStateOf(false) }

    BaseListItem(
        rowHeight = 56.dp,
        onClick = if (isTheme) null else onClick,
        modifier =
            modifier
                .fillMaxWidth()
                .then(
                    if (!isTheme) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                )
                .padding(horizontal = 16.dp),
        content = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        trail = {
            if (isTheme) {
                Switch(
                    modifier = Modifier.size(52.dp, 32.dp),
                    checked = checked,
                    onCheckedChange = { checked = it },
                    colors =
                        SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.inverseSurface,
                            checkedTrackColor = MaterialTheme.colorScheme.inverseOnSurface,
                            uncheckedTrackColor = MaterialTheme.colorScheme.inverseOnSurface,
                            uncheckedBorderColor = MaterialTheme.colorScheme.inverseSurface,
                            checkedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.triangle_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
    )
}
