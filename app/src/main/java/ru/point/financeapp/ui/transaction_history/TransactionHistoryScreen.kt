package ru.point.financeapp.ui.transaction_history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.point.financeapp.R
import ru.point.core.ui.ActionState
import ru.point.core.ui.BackAction
import ru.point.core.ui.BackState
import ru.point.core.ui.BaseScaffold
import ru.point.core.ui.FabState
import ru.point.core.ui.TopBarAction

@Preview()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen() {

    BaseScaffold(
        title = stringResource(R.string.my_transaction_history),
        backState = BackState.Shown,
        backAction = BackAction {  },
        action = TopBarAction(
            iconRes = R.drawable.transaction_analys_icon,
            contentDescription = "Анализ",
            onClick = {}
        ),
        actionState = ActionState.Shown,
        fabState = FabState.Hidden,
    )  { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
//                items(settings) { setting ->
//                    SettingsRow(
//                        title = setting.title,
//                        isTheme = setting.isTheme,
//                        onClick = { /* навигация */ },
//                        modifier = Modifier
//                    )
//                    HorizontalDivider(
//                        modifier = Modifier,
//                        color = MaterialTheme.colorScheme.surfaceDim,
//                        thickness = 1.dp
//                    )
//                }
            }
        }
    }
}