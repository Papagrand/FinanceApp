package ru.point.categories.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.categories.R
import ru.point.categories.presentation.mvi.CategoriesEffect
import ru.point.categories.presentation.mvi.CategoriesIntent
import ru.point.categories.presentation.mvi.CategoriesViewModel
import ru.point.navigation.Navigator
import ru.point.ui.composables.ActionState
import ru.point.ui.composables.BaseScaffold
import ru.point.ui.composables.FabState
import ru.point.ui.composables.NoInternetBanner
import ru.point.ui.di.LocalViewModelFactory
import ru.point.utils.network.NetworkHolder

/**
 * CategoryScreen
 *
 * Ответственность:
 * - отображение экрана категорий с полем поиска, индикатором загрузки и списком данных;
 * - отправка MVI-интентов (Load, Retry, Search) и реакция на эффекты (показ Snackbar);
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navigator: Navigator,
    onAddClick: () -> Unit = {},
) {
    val viewModel: CategoriesViewModel = viewModel(factory = LocalViewModelFactory.current)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val tracker = remember { NetworkHolder.tracker }

    LaunchedEffect(Unit) {
        viewModel.dispatch(CategoriesIntent.Load)
        viewModel.effect.collect { effect ->
            when (effect) {
                is CategoriesEffect.ShowSnackbar ->
                    snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    val text = remember { mutableStateOf("") }

    BaseScaffold(
        title = stringResource(R.string.my_categories),
        action = null,
        actionState = ActionState.Hidden,
        fabState = FabState.Hidden,
    ) { innerPadding ->

        NoInternetBanner(tracker = tracker)

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            CategorySearch(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(MaterialTheme.colorScheme.outline),
                value = state.query,
                onValueChange = { new ->
                    text.value = new
                    viewModel.dispatch(CategoriesIntent.Search(new))
                },
                placeHolderResId = R.string.search_placeholder,
            )

            HorizontalDivider(
                modifier = Modifier,
                color = MaterialTheme.colorScheme.surfaceDim,
                thickness = 1.dp,
            )

            when {
                state.isLoading -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(top = 32.dp),
                        contentAlignment = Alignment.TopCenter,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${state.error}",
                    )
                    Log.e("errorEx", state.error.toString())
                }

                else -> {
                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxSize(),
                    ) {
                        items(state.list) { selection ->
                            CategoryRow(
                                modifier = Modifier,
                                selection,
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
    }
}

fun initialsOf(title: String): String {
    val words = title.trim().split("\\s+".toRegex())
    val first = words.getOrNull(0)?.firstOrNull()?.uppercase() ?: ""
    val second = words.getOrNull(1)?.firstOrNull()?.uppercase() ?: ""
    return buildString {
        append(first)
        if (second.isNotEmpty()) append(second)
    }
}
