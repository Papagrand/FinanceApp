package ru.point.settings.settingsScreen.ui.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.navigation.Navigator
import ru.point.navigation.Route
import ru.point.settings.R
import ru.point.settings.di.SettingsDepsStore
import ru.point.settings.settingsScreen.di.DaggerSettingsComponent
import ru.point.settings.settingsScreen.ui.mvi.SettingsIntent
import ru.point.settings.settingsScreen.ui.mvi.SettingsViewModel
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navigator: Navigator) {

    val settingsComponent =
        remember {
            DaggerSettingsComponent
                .builder()
                .deps(settingsDeps = SettingsDepsStore.settingsDeps)
                .build()
        }

    val viewModel = viewModel<SettingsViewModel>(factory = settingsComponent.settingsViewModelFactory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseScaffold(
        title = stringResource(R.string.settings),
        action = null,
        actionState = ActionState.Hidden,
        fabState = FabState.Hidden,
    ) { innerPadding ->

        SettingsColumn(
            state = state,
            innerPadding = innerPadding,
            onToggleTheme = { viewModel.dispatch(SettingsIntent.ToggleDarkTheme) },
            onClickNavigateToMainColor = { navigator.navigate(Route.MainColor) },
            onClickNavigateToAppInfo = { navigator.navigate(Route.AppInfo) }
        )
    }
}
