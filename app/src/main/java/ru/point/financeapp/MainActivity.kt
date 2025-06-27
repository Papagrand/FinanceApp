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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.compose.HomeWork1Theme
import ru.point.core.di.LocalViewModelFactory
import javax.inject.Inject

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
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MainActivityViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent.inject(this)

        installSplashScreen().setKeepOnScreenCondition {
            !viewModel.dataCollected.value
        }
        enableEdgeToEdge()

        setContent {
            CompositionLocalProvider(
                LocalViewModelFactory provides viewModelFactory,
            ) {
                HomeWork1Theme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        MainActivityUI(viewModel)
                    }
                }
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
)
