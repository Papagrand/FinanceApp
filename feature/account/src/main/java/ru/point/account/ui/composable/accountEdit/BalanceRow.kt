package ru.point.account.ui.composable.accountEdit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.point.ui.composables.SimpleListRow

@Composable
fun BalanceRow(
    balance: String,
    onChange: (String) -> Unit,
) {
    SimpleListRow(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        content = {
            Text(
                "Баланс",
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        trail = {
            TextField(
                value = balance,
                onValueChange = onChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle =
                    MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.End,
                    ),
                colors =
                    TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
            )
        },
    )
}

@Preview(
    name = "BalanceRow",
    showBackground = true,
    backgroundColor = 0xFFF0F0F0,
)
@Composable
fun BalanceRowPreview() {
    var text by remember { mutableStateOf("Баланс") }

    MaterialTheme {
        BalanceRow(
            balance = "35550.00",
            onChange = { text = it },
        )
    }
}
