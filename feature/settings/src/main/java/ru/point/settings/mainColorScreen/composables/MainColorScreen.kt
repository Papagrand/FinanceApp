package ru.point.settings.mainColorScreen.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.point.settings.R
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainColorScreen() {

//    val settingsComponent =
//        remember {
//            DaggerSettingsComponent
//                .builder()
//                .deps(settingsDeps = SettingsDepsStore.settingsDeps)
//                .build()
//        }
//
//    val viewModel = viewModel<SettingsViewModel>(factory = settingsComponent.settingsViewModelFactory)
//
//    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseScaffold(
        title = stringResource(R.string.settings),
        action = null,
        actionState = ActionState.Hidden,
        fabState = FabState.Hidden,
    ) { innerPadding ->

    }
}
