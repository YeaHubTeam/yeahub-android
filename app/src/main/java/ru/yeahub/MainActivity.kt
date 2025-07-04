package ru.yeahub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.navigation_impl.AppNavigation

/**
 * Главная активити приложения, которая инициализирует навигацию.
 * Навигация:
 * 1. Все экраны приложения отображаются через [AppNavigation]
 * 2. Нижняя навигация настраивается в [NavigationFactory]
 * 3. Маршруты определены в [NavigationRoutes]
 * Добавление нового экрана:
 * 1. Если экран должен быть в нижней навигации:
 *    - Добавьте маршрут в [NavigationRoutes.Bottom]
 *    - Создайте новый объект в [BottomNavigationItem]
 *    - Добавьте item в [NavigationFactory.getBottomNavItems]
 *    - Добавьте composable в [AppNavigation]
 * 2. Если экран является частью feature-модуля:
 *    - Добавьте маршрут в [NavigationRoutes.Feature]
 *    - Создайте API интерфейс экрана в feature/example-your-feature/api
 *    - Реализуйте экран в feature/example-your-feature/impl
 *    - Добавьте навигацию к экрану через NavController
 * 3. Для вложенной навигации внутри фичи:
 *    - Используйте вложенные маршруты (например, "profile/settings")
 *    - Добавьте маршруты в [NavigationRoutes.Feature]
 *    - Используйте NavController для навигации между экранами
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YeaHubTheme {
                AppNavigation()
            }
        }
    }
}