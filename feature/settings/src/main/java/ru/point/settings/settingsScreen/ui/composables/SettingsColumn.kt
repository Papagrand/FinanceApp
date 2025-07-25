package ru.point.settings.settingsScreen.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.point.settings.R
import ru.point.settings.settingsScreen.ui.mvi.SettingsState

@Composable
internal fun SettingsColumn(
    state: SettingsState,
    innerPadding: PaddingValues,
    onToggleTheme: () -> Unit,
    onClickNavigateToMainColor: () -> Unit,
    onClickNavigateToAppInfo: () -> Unit
) {

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
    ) {

        SettingsRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            placeHolderResId = R.string.theme,
            isTheme = true,
            checked = state.isDarkTheme,
            onCheckedChange = { onToggleTheme() }
        )

        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )

        SettingsRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            placeHolderResId = R.string.main_color,
            isTheme = false,
            onClick = onClickNavigateToMainColor
        )

        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )

        SettingsRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            placeHolderResId = R.string.about_app,
            isTheme = false,
            onClick = onClickNavigateToAppInfo
        )

        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )

    }

}