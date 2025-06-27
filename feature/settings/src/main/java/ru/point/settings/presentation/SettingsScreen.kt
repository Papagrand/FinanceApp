package ru.point.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.point.core.ui.ActionState
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.navigation.Navigator
import ru.point.settings.R
import ru.point.settings.domain.SettingsUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navigator: Navigator) {
    val settings = remember { demoSettings() }

    BaseScaffold(
        title = stringResource(R.string.settings),
        action = null,
        actionState = ActionState.Hidden,
        fabState = FabState.Hidden,
    ) { innerPadding ->

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize(),
            ) {
                items(settings) { setting ->
                    SettingsRow(
                        title = setting.title,
                        isTheme = setting.isTheme,
                        onClick = { /* навигация */ },
                        modifier = Modifier,
                    )
                    HorizontalDivider(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        thickness = 1.dp,
                    )
                }
            }
        }
    }
}

private fun demoSettings(): List<SettingsUi> =
    listOf(
        SettingsUi(title = "Тёмная тема", true),
        SettingsUi(title = "Основной цвет"),
        SettingsUi(title = "Звуки"),
        SettingsUi(title = "Хаптики"),
        SettingsUi(title = "Код пароль"),
        SettingsUi(title = "Cинхронизация"),
        SettingsUi(title = "Язык"),
        SettingsUi(title = "О программе"),
    )
