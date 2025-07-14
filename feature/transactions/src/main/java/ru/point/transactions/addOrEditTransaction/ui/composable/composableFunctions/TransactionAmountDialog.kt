package ru.point.transactions.addOrEditTransaction.ui.composable.composableFunctions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.point.transactions.R
import ru.point.utils.extensionsAndParsers.sanitizeDecimalInput
import ru.point.utils.extensionsAndParsers.validateBalance

@Composable
internal fun TransactionAmountDialog(
    title: String,
    initialValue: String,
    keyBoardType: KeyboardType,
    maxLength: Int,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    showTextCounter: Boolean = false,
) {
    var text by remember { mutableStateOf(initialValue) }
    var errorText by remember { mutableStateOf<String?>(null) }

    fun validate(input: String) {
        val sanitized = input.sanitizeDecimalInput()
        val intPart = sanitized.substringBefore('.', sanitized)
        errorText =
            when {
                sanitized.isBlank() ->
                    "Введите сумму"
                intPart.length > maxLength ->
                    "Не более $maxLength символов до запятой"
                input.validateBalance() == null ->
                    "Формат: 12345,67"
                else ->
                    null
            }
    }

    LaunchedEffect(initialValue) {
        validate(initialValue)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.secondaryContainer,
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = keyBoardType,
                            imeAction = ImeAction.Done,
                        ),
                    value = text,
                    onValueChange = { input ->
                        if (input.length <= maxLength + 3 && !input.startsWith(" ")) {
                            text = input.sanitizeDecimalInput()
                            validate(text)
                        }

                        val sanitized =
                            if (keyBoardType == KeyboardType.Number) {
                                input.sanitizeDecimalInput()
                            } else {
                                input
                            }

                        val intPart = sanitized.substringBefore('.', sanitized)
                        if (intPart.length <= maxLength) {
                            text = sanitized
                        }
                    },
                    singleLine = true,
                    isError = errorText != null,
                    supportingText = {
                        errorText?.let { msg ->
                            Text(
                                text = msg,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        }
                        if (showTextCounter) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(end = 4.dp),
                                contentAlignment = Alignment.CenterEnd,
                            ) {
                                Text(
                                    text = "${text.length} / $maxLength",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    },
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            selectionColors =
                                TextSelectionColors(
                                    handleColor = MaterialTheme.colorScheme.primary,
                                    backgroundColor = MaterialTheme.colorScheme.background,
                                ),
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        onClick = onDismiss,
                        colors =
                            ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }

                    TextButton(
                        onClick = { onConfirm(text) },
                        enabled = (errorText == null),
                        colors =
                            ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                    ) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}
