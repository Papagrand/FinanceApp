package ru.point.transactions.addOrEditTransaction.ui.composable.composableFunctions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.point.transactions.addOrEditTransaction.ui.mvi.AddOrEditTransactionIntent
import ru.point.transactions.addOrEditTransaction.ui.mvi.AddOrEditTransactionState
import ru.point.ui.composables.NoInternetBanner
import ru.point.ui.composables.SimpleListRow
import ru.point.utils.extensionsAndParsers.ScreenEnums

@Composable
internal fun AddOrEditElementsColumn(
    lastUpdate: String,
    innerPadding: PaddingValues,
    accountName: String?,
    isOnline: Boolean,
    onCommentChange: (String?) -> Unit,
    onDeleteClick: () -> Unit,
    onIntent: (AddOrEditTransactionIntent) -> Unit,
    state: AddOrEditTransactionState,
) {
    var localComment by rememberSaveable(state.comment) {
        mutableStateOf(state.comment)
    }

    var showAmountDialog by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
    ) {
        if (!isOnline){
            NoInternetBanner()

            HorizontalDivider(
                modifier = Modifier,
                color = MaterialTheme.colorScheme.surfaceDim,
                thickness = 1.dp,
            )
        }

        if (state.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = .50f)),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        if (state.error != null && isOnline) {
            AlertDialog(
                onDismissRequest = { onIntent(AddOrEditTransactionIntent.ClearError) },
                title = { Text("Упс, что-то пошло не так :(") },
                text = { Text(state.error) },
                confirmButton = {
                    TextButton(onClick = { onIntent(AddOrEditTransactionIntent.Retry) }) {
                        Text("Повторить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onIntent(AddOrEditTransactionIntent.ClearError) }) {
                        Text("Закрыть")
                    }
                },
            )
        }

        SimpleListRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            content = { Text("Счёт") },
            trail = {
                if (state.accountName != "") {
                    Text(state.accountName)
                } else {
                    Text(accountName ?: "")
                }
            },
        )

        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )

        CategoryPickerRow(
            picked = state.pickedCategory,
            all = state.allCategories,
            onPick = { picked ->
                onIntent(AddOrEditTransactionIntent.CategoryPicked(picked))
            },
        )

        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )

        TransactionAmountRow(
            amountRaw = state.amountInput,
            onEdit = { showAmountDialog = true },
        )

        if (showAmountDialog) {
            TransactionAmountDialog(
                title = "Редактировать сумму",
                initialValue = state.amountInput,
                keyBoardType = KeyboardType.Number,
                maxLength = 13,
                showTextCounter = false,
                onDismiss = { showAmountDialog = false },
                onConfirm = { clean ->
                    onIntent(AddOrEditTransactionIntent.AmountChanged(clean))
                    showAmountDialog = false
                },
            )
        }

        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )

        TransactionDatePicker(
            modifier =
                Modifier
                    .fillMaxWidth(),
            title = "Дата",
            currentOrEmptyDate = state.date,
            onDateChange = { changed ->
                onIntent(AddOrEditTransactionIntent.DateChanged(changed))
            },
        )

        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )

        TransactionTimePicker(
            modifier = Modifier,
            currentOrEmptyTime = state.time,
            onTimeChange = { changed ->
                onIntent(AddOrEditTransactionIntent.TimeChanged(changed))
            },
        )

        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )

        SimpleListRow(
            modifier = Modifier.fillMaxWidth(),
            content = {
                TextField(
                    value = localComment ?: "",
                    onValueChange = { new ->
                        localComment = new
                        if (new != "") {
                            onCommentChange(new)
                        } else {
                            onCommentChange(null)
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    placeholder = {
                        Text(
                            text = "Впишите комментарий",
                            style =
                                MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors =
                        TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.background,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        ),
                    modifier = Modifier.width(250.dp),
                )
            },
            trail = {},
        )

        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.surfaceDim,
            thickness = 1.dp,
        )

        if (state.screenMode == ScreenEnums.EDIT) {
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onDeleteClick,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .height(40.dp),
                shape = RoundedCornerShape(24.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                contentPadding = PaddingValues(0.dp),
            ) {
                Text(
                    text = if (state.isIncome) "Удалить доход" else "Удалить расход",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Последняя синхронизация: $lastUpdate",
                textAlign = TextAlign.Center
            )
        }
    }
}
