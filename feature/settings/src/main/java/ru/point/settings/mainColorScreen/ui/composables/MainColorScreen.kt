package ru.point.settings.mainColorScreen.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.navigation.Navigator
import ru.point.settings.R
import ru.point.settings.di.SettingsDepsStore
import ru.point.settings.mainColorScreen.di.DaggerMainColorComponent
import ru.point.settings.mainColorScreen.ui.mvi.MainColorIntent
import ru.point.settings.mainColorScreen.ui.mvi.MainColorViewModel
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BackAction
import ru.point.ui.composables.BackState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainColorScreen(
    navigator: Navigator,
) {

    val mainColorComponent =
        remember {
            DaggerMainColorComponent
                .builder()
                .deps(settingsDeps = SettingsDepsStore.settingsDeps)
                .build()
        }

    val viewModel = viewModel<MainColorViewModel>(factory = mainColorComponent.mainColorViewModelFactory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val palettes = listOf(
        "green" to Color(0xFF2AE881),
        "purple" to Color(0xFFA334E5),
        "blue" to Color(0xFF3D70E5),
        "orange" to Color(0xFFFF992E)
    )

    BaseScaffold(
        title = stringResource(R.string.select_color),
        actionState = ActionState.Hidden,
        fabState = FabState.Hidden,
        backState = BackState.Shown,
        backAction = BackAction(onClick = navigator::navigateUp),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            palettes.forEach { (name, color) ->
                ColorCircle(
                    color = color,
                    selected = name == state.pickedColor,
                    onClick = { viewModel.dispatch(MainColorIntent.PickNewColor(name)) }
                )
            }
        }
    }
}
