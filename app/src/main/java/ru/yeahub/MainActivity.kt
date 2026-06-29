package ru.yeahub

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.android.inject
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.navigation_impl.AppNavigation
import ru.yeahub.navigation_impl.NotificationNavigationService
import timber.log.Timber

/**
 * Главная активити приложения, которая инициализирует навигацию.
 * 
 * Поддерживает:
 * - Обычную навигацию через AppNavigation
 * - Deep links из уведомлений
 * - Правильную обработку Intent-ов при создании и возобновлении активности
 * 
 * Навигация:
 * 1. Все экраны приложения отображаются через [AppNavigation]
 * 2. Нижняя навигация настраивается в [NavigationFactory]
 * 3. Маршруты определены в [NavigationRoutes]
 * 4. Deep links обрабатываются через [NotificationNavigationService]
 * 
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

    private val notificationService: NotificationNavigationService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            YeaHubTheme {
                val navController = rememberNavController()
                var pendingIntent by remember { mutableStateOf<Intent?>(null) }
                
                // Обрабатываем intent при создании активности
                LaunchedEffect(Unit) {
                    intent?.let { initialIntent ->
                        if (notificationService.canHandleIntent(initialIntent)) {
                            pendingIntent = initialIntent
                        }
                    }
                }
                
                // Обрабатываем pending intent после инициализации навигации
                LaunchedEffect(pendingIntent) {
                    pendingIntent?.let { intentToHandle ->
                        Timber.d("MainActivity onCreate: Handling pending intent: ${intentToHandle.data}")
                        notificationService.handleNotificationIntent(intentToHandle, navController)
                        pendingIntent = null
                    }
                }
                
                AppNavigation(navController = navController)
            }
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        
        Timber.d("MainActivity onNewIntent: Received intent: ${intent.data}")
        
        // Обрабатываем новый intent, если активность уже запущена
        if (notificationService.canHandleIntent(intent)) {
            // Получаем текущий navController из compose
            // Это будет обработано в следующем цикле композиции
            this.intent = intent
        }
    }
    
    /**
     * Получает текущий intent для обработки в compose.
     * 
     * @return Intent для обработки или null
     */
    fun getPendingIntentForCompose(): Intent? {
        return if (notificationService.canHandleIntent(intent)) {
            intent
        } else {
            null
        }
    }
}