package ru.point.settings.settingsScreen.ui.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.point.settings.R
import ru.point.ui.composables.BaseListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsRow(
    @StringRes placeHolderResId: Int,
    isTheme: Boolean,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    modifier: Modifier,
    onClick: () -> Unit = {}
) {

    BaseListItem(
        onClick = if (isTheme) null else onClick,
        modifier =
            modifier
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
                text = stringResource(placeHolderResId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        trail = {
            if (isTheme) {
                Switch(
                    modifier = Modifier.size(52.dp, 32.dp),
                    checked = checked,
                    onCheckedChange = onCheckedChange,
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
