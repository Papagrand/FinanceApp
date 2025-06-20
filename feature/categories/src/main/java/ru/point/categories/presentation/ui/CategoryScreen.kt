package ru.point.categories.presentation.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.point.categories.R
import ru.point.categories.data.repositoryImpl.CategoryRepositoryImpl
import ru.point.categories.domain.model.Category
import ru.point.categories.domain.usecase.ObserveCategoriesUseCase
import ru.point.categories.presentation.mvi.CategoriesEffect
import ru.point.categories.presentation.mvi.CategoriesIntent
import ru.point.categories.presentation.mvi.CategoriesViewModel
import ru.point.categories.presentation.mvi.CategoriesViewModelFactory
import ru.point.core.ui.ActionState
import ru.point.core.ui.BaseListItem
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.core.ui.NoInternetBanner
import ru.point.core.utils.NetworkHolder
import ru.point.navigation.Navigator
import ru.point.network.client.RetrofitProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navigator: Navigator,
    onAddClick: () -> Unit = {}
) {

    val repo = CategoryRepositoryImpl(RetrofitProvider.instance)
    val useCase = ObserveCategoriesUseCase(repo)
    val factory = remember { CategoriesViewModelFactory(useCase) }

    val viewModel: CategoriesViewModel = viewModel(factory = factory)

    val state by viewModel.state.collectAsState()

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
        fabState = FabState.Hidden
    ) { innerPadding ->

        NoInternetBanner(tracker = tracker)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            var query by remember { mutableStateOf("") }
            SelectionSearch(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.outline),
                value = text.value,
                onValueChange = { new ->
                    query = new
                    viewModel.dispatch(CategoriesIntent.Search(new))
                },
                placeHolderResId = R.string.search_placeholder
            )

            HorizontalDivider(
                modifier = Modifier,
                color = MaterialTheme.colorScheme.surfaceDim,
                thickness = 1.dp
            )

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.TopCenter
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
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(state.list) { selection ->
                            SelectionRow(
                                modifier = Modifier,
                                selection
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
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionSearch(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeHolderResId: Int,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = stringResource(placeHolderResId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        singleLine = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        shape = RectangleShape,
        textStyle = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
fun SelectionRow(
    modifier: Modifier,
    category: Category,
    onClick: () -> Unit = {}
) = BaseListItem(
    rowHeight = 70.dp,
    onClick = onClick,
    modifier = modifier
        .fillMaxWidth()
        .clickable(onClick = { })
        .padding(horizontal = 16.dp),
    lead = {
        val initials = remember(category.name) { initialsOf(category.name) }
        val iconText = category.emoji ?: initials

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iconText,
                style = if (category.emoji != null)
                    MaterialTheme.typography.bodyLarge
                else
                    TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.0.sp
                    )
            )
        }
    },

    content = {
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
)

fun initialsOf(title: String): String {
    val words = title.trim().split("\\s+".toRegex())
    val first = words.getOrNull(0)?.firstOrNull()?.uppercase() ?: ""
    val second = words.getOrNull(1)?.firstOrNull()?.uppercase() ?: ""
    return buildString {
        append(first)
        if (second.isNotEmpty()) append(second)
    }
}