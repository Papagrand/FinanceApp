package ru.point.account.ui.composable.accountEdit

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.point.account.R
import ru.point.ui.composables.SimpleListRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyRow(
    current: String,
    onSelect: (String) -> Unit,
    allCurrencies: List<CurrencyUi>,
) {
    val showSheet = remember { mutableStateOf(false) }

    SimpleListRow(
        modifier =
            Modifier
                .clickable { showSheet.value = true }
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        content = { Text("Валюта") },
        trail = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(current)
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.right_arrow),
                    contentDescription = null,
                )
            }
        },
    )

    if (showSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showSheet.value = false },
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            allCurrencies.forEach { item ->
                SimpleListRow(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable {
                                onSelect(item.code)
                                showSheet.value = false
                            },
                    lead = {
                        Icon(
                            painter = painterResource(item.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    },
                    content = {
                        Text(item.fullName)
                    },
                )
                HorizontalDivider(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.surfaceDim,
                    thickness = 1.dp,
                )
            }

            SimpleListRow(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.error)
                        .clickable { showSheet.value = false },
                lead = {
                    Icon(
                        painter = painterResource(R.drawable.cancel),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier.size(24.dp),
                    )
                },
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

data class CurrencyUi(
    val code: String,
    val fullName: String,
    @DrawableRes val iconRes: Int,
)
