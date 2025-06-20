package ru.point.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.point.core.ui.ActionState
import ru.point.core.ui.BaseListItem
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.navigation.Navigator
import ru.point.settings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigator: Navigator,
) {
    val settings = remember { demoSettings() }

    BaseScaffold(
        title = stringResource(R.string.settings),
        action = null,
        actionState = ActionState.Hidden,
        fabState = FabState.Hidden
    )  { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(settings) { setting ->
                    SettingsRow(
                        title = setting.title,
                        isTheme = setting.isTheme,
                        onClick = { /* навигация */ },
                        modifier = Modifier
                    )
                    HorizontalDivider(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}


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
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (!isTheme) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .padding(horizontal = 16.dp),

        content = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },

        trail = {
            if (isTheme) {
                Switch(
                    modifier = Modifier.size(52.dp, 32.dp),
                    checked = checked,
                    onCheckedChange = { checked = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.inverseSurface,
                        checkedTrackColor = MaterialTheme.colorScheme.inverseOnSurface,
                        uncheckedTrackColor = MaterialTheme.colorScheme.inverseOnSurface,
                        uncheckedBorderColor = MaterialTheme.colorScheme.inverseSurface,
                        checkedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.triangle_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

private data class SettingUi(
    val title: String,
    val isTheme: Boolean = false
)

private fun demoSettings(): List<SettingUi> = listOf(
    SettingUi(title = "Тёмная тема", true),
    SettingUi(title = "Основной цвет"),
    SettingUi(title = "Звуки"),
    SettingUi(title = "Хаптики"),
    SettingUi(title = "Код пароль"),
    SettingUi(title = "Cинхронизация"),
    SettingUi(title = "Язык"),
    SettingUi(title = "О программе")
)
