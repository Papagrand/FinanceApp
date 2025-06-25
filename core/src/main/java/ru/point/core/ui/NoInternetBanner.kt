package ru.point.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.point.core.utils.NetworkTracker
import androidx.compose.runtime.getValue

@Composable
fun NoInternetBanner(tracker: NetworkTracker) {
    val online by tracker.online.collectAsState(initial = true)

    AnimatedVisibility(visible = !online) {
        Surface(
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Нет подключения к интернету",
                color = MaterialTheme.colorScheme.onError,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}