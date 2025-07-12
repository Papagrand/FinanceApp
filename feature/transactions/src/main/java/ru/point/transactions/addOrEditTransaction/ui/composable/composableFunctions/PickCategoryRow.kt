package ru.point.transactions.addOrEditTransaction.ui.composable.composableFunctions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.point.api.model.AllCategoriesDto
import ru.point.ui.composables.SimpleListRow

/**
 * CategoryRow
 *
 * Ответственность:
 * - базовый элемент для отображения категории
 */

@Composable
internal fun PickCategoryRow(
    category: AllCategoriesDto,
    onClick: () -> Unit,
) = SimpleListRow(
    rowHeight = 70.dp,
    onClick = onClick,
    modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    lead = {
        val iconText = category.emoji

        Box(
            modifier =
                Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = iconText,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    },
    content = {
        Text(
            text = category.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    },
)
