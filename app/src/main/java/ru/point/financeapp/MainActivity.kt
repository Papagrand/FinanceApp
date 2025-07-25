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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ru.point.ui.theme.FinanceAppTheme
import javax.inject.Inject
import ru.point.api.flow.ThemePreferencesRepo
import ru.point.financeapp.di.utils.ViewModelFactory
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

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<MainActivityViewModel> { viewModelFactory }

    @Inject
    lateinit var networkTracker: NetworkTracker

    @Inject
    lateinit var themeRepo: ThemePreferencesRepo

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
                val isDark by themeRepo.isDarkThemeFlow.collectAsState(initial = false)
                val themeName by themeRepo.primaryColorNameFlow.collectAsState(initial = "green")
                FinanceAppTheme(darkTheme = isDark, theme = themeName) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        MainActivityUI()
                    }
                }
            }
        }

    }
}
