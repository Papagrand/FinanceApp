package ru.point.transactions.addOrEditTransaction.ui.composable.composableFunctions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.point.api.flow.toCategoryDto
import ru.point.api.model.AllCategoriesDto
import ru.point.api.model.CategoryDto
import ru.point.transactions.R
import ru.point.ui.composables.SimpleListRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoryPickerRow(
    picked: CategoryDto?,
    all: List<AllCategoriesDto>,
    onPick: (CategoryDto) -> Unit,
) {
    var showSheet by remember { mutableStateOf(false) }

    SimpleListRow(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { showSheet = true }
                .padding(horizontal = 16.dp),
        content = { Text("Статья") },
        trail = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(picked?.categoryName ?: "")
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.right_arrow),
                    contentDescription = null,
                )
            }
        },
    )

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            LazyColumn(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
            ) {
                items(all) { cat ->
                    PickCategoryRow(
                        category = cat,
                        onClick = {
                            onPick(cat.toCategoryDto())
                            showSheet = false
                        },
                    )
                    HorizontalDivider(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceDim,
                        thickness = 1.dp,
                    )
                }
            }

            SimpleListRow(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.error)
                        .clickable { showSheet = false }
                        .padding(horizontal = 16.dp),
                content = {
                    Text(
                        text = "Отмена",
                        color = MaterialTheme.colorScheme.background,
                    )
                },
            )
        }
    }
}
