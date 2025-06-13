package ru.point.homework1.ui.screens.selection

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import ru.point.homework1.R
import ru.point.homework1.ui.screens.ActionState
import ru.point.homework1.ui.screens.BaseListItem
import ru.point.homework1.ui.screens.BaseScaffold
import ru.point.homework1.ui.screens.FabState

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionScreen(
    onAddClick: () -> Unit = {}
) {
    val selections = remember { demoCategory() }

    val text = remember { mutableStateOf("") }

    BaseScaffold(
        title = stringResource(R.string.my_categories),
        action = null,
        actionState = ActionState.Hidden,
        fabState = FabState.Hidden
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SelectionSearch(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.outline),
                value = text.value,
                onValueChange = { text.value = it },
                placeHolderResId = R.string.search_placeholder
            )

            HorizontalDivider(
                modifier = Modifier,
                color = MaterialTheme.colorScheme.surfaceDim,
                thickness = 1.dp
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(selections) { selection ->
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
    },

    trail = {
    }
)

private fun demoCategory(): List<Category> = listOf(
    Category(
        id = 0,
        name = "Аренда квартиры",
        emoji = "🏡",
        isIncome = true

    ),
    Category(
        id = 1,
        name = "Одежда",
        emoji = "👗",
        isIncome = true
    ),
    Category(
        id = 2,
        name = "На собачку",
        emoji = "🐶",
        isIncome = true

    ),
    Category(
        id = 3,
        name = "На собачку",
        emoji = "🐶",
        isIncome = true
    ),
    Category(
        id = 4,
        name = "Ремонт квартиры",
        emoji = null,
        isIncome = true
    ),
    Category(
        id = 5,
        name = "Продукты",
        emoji = "🍭",
        isIncome = true
    ),
    Category(
        id = 6,
        name = "Спортзал",
        emoji = "🏋️",
        isIncome = true
    ),
    Category(
        id = 7,
        name = "Медицина",
        emoji = "💊",
        isIncome = true
    )
)

private fun initialsOf(title: String): String {
    val words = title.trim().split("\\s+".toRegex())
    val first = words.getOrNull(0)?.firstOrNull()?.uppercase() ?: ""
    val second = words.getOrNull(1)?.firstOrNull()?.uppercase() ?: ""
    return buildString {
        append(first)
        if (second.isNotEmpty()) append(second)
    }
}

