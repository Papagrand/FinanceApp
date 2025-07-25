package ru.point.settings.appInfo.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.navigation.Navigator
import ru.point.settings.R
import ru.point.settings.appInfo.di.DaggerAppInfoComponent
import ru.point.settings.appInfo.ui.mvi.AppInfoIntent
import ru.point.settings.appInfo.ui.mvi.AppInfoViewModel
import ru.point.settings.di.SettingsDepsStore
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BackAction
import ru.point.ui.composables.BackState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoScreen(
    navigator: Navigator,
) {

    val appInfoComponent =
        remember {
            DaggerAppInfoComponent
                .builder()
                .deps(settingsDeps = SettingsDepsStore.settingsDeps)
                .build()
        }

    val viewModel = viewModel<AppInfoViewModel>(factory = appInfoComponent.appInfoViewModelFactory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.dispatch(AppInfoIntent.LoadVersion)
        viewModel.dispatch(AppInfoIntent.LoadLastUpdate)
    }

    BaseScaffold(
        title = stringResource(R.string.about_app),
        actionState = ActionState.Hidden,
        fabState = FabState.Hidden,
        backState = BackState.Shown,
        backAction = BackAction(onClick = navigator::navigateUp),
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_version, state.appVersion),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(R.string.last_time_update, state.lastTimeUpdate),
                    textAlign = TextAlign.Center,
                )
            }

        }
    }
}
