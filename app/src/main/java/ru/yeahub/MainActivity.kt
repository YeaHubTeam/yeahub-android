package ru.yeahub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.navigation_impl.AppNavigation
import com.example.navigation_impl.NavigationFactory
import ru.yeahub.core_ui.theme.YeaHubTheme

/**
 * КАК ДОБАВИТЬ НОВУЮ ФИЧУ:
 * 1. Создайте класс, реализующий FeatureNavigation
 * 2. Добавьте экземпляр в NavigationFactory.getFeatureNavigations()
 * 3. Фича автоматически появится в bottom navigation
 * ПРИМЕР ДОБАВЛЕНИЯ ФИЧИ:
 * ```kotlin
 * // В NavigationFactory.getFeatureNavigations()
 * return listOf(
 *     HomeFeatureNavigation(),
 *     ProfileFeatureNavigation(),
 *     NewFeatureNavigation() // Добавьте новую фичу
 * )
 * ```
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YeaHubTheme {
                AppNavigation(
                    modifier = Modifier.fillMaxSize(),
                    featureNavigations = NavigationFactory.getFeatureNavigations()
                )
            }
        }
    }
}