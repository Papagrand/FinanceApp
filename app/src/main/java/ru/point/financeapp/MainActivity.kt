package ru.point.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.compose.HomeWork1Theme
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import ru.point.impl.work.PushAccountPendingWorker
import ru.point.impl.work.PushPendingWorker
import ru.point.ui.di.LocalInternetTracker
import ru.point.utils.network.NetworkTracker

/**
 * MainActivity
 * Главная Activity-прослойка, хост всего Compose.
 *
 *  - Инжектит ViewModelFactory Dagger’ом;
 *  - Устанавливает флаг SplashScreen до dataCollected;
 *  - Прокидывает CompositionLocal с ViewModelFactory;
 *  - Отрисовывает BottomBar и NavGraph внутри Scaffold.
 */

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainActivityViewModel>()

    @Inject lateinit var networkTracker: NetworkTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(this)

        installSplashScreen().setKeepOnScreenCondition {
            !viewModel.dataCollected.value
        }
        enableEdgeToEdge()

        setContent {
            CompositionLocalProvider(
                LocalInternetTracker provides networkTracker,
            ) {
                HomeWork1Theme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        MainActivityUI()
                    }
                }
            }
        }

        scheduleSync()
    }

    private fun scheduleSync() {
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "PushPending",
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<PushPendingWorker>(15, TimeUnit.MINUTES)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .build()
            )

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "PushAccountPending",
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<PushAccountPendingWorker>(18, TimeUnit.MINUTES)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .build()
            )
    }
}
