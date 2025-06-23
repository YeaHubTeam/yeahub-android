package com.example.navigation_impl

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.navigation_api.NavigationApi
import com.example.navigation_api.NavigationRoutes
import com.example.navigation_api.FeatureNavigation

/**
 * Реализация навигации приложения
 * 
 * АРХИТЕКТУРА:
 * Этот класс реализует NavigationApi и управляет регистрацией всех фич.
 * Основная логика навигации находится в AppNavigation composable.
 * 
 * ПРИНЦИП РАБОТЫ:
 * 1. Принимает список FeatureNavigation в конструкторе
 * 2. Создает главный NavHost с экраном MAIN
 * 3. Экраны фич регистрируются в вложенном NavHost в MainNavigationScreen
 * 
 * ИСПОЛЬЗОВАНИЕ:
 * Не создавайте экземпляры напрямую. Используйте NavigationFactory:
 * ```kotlin
 * val navigationApi = NavigationFactory.createNavigation()
 * ```
 */
class NavigationImpl(
    private val featureNavigations: List<FeatureNavigation>
) : NavigationApi {
    
    /**
     * Регистрирует навигационные графы для фич
     * 
     * ПРИМЕЧАНИЕ:
     * Этот метод вызывается автоматически в AppNavigation.
     * Не вызывайте его вручную.
     * 
     * ПАРАМЕТРЫ:
     * @param navController - контроллер навигации для регистрации графов
     */
    override fun registerFeatureGraphs(navController: NavHostController) {
        // Регистрация графов фич происходит в MainNavigationScreen
        // Каждая фича регистрирует свой граф через FeatureNavigation.registerGraph()
    }
    
    /**
     * Возвращает стартовый маршрут приложения
     * 
     * ВОЗВРАЩАЕТ:
     * Маршрут главного экрана с bottom navigation
     */
    override fun getStartDestination(): String = NavigationRoutes.MAIN
}

/**
 * Основной NavHost приложения
 * 
 * АРХИТЕКТУРА:
 * Этот composable является корнем навигации приложения.
 * Он создает главный NavHost и регистрирует главный экран.
 * 
 * СТРУКТУРА:
 * 1. Главный экран (MAIN) - содержит bottom navigation и вложенный NavHost
 * 2. Экраны фич регистрируются в вложенном NavHost в MainNavigationScreen
 * 
 * ПАРАМЕТРЫ:
 * @param modifier - модификатор для настройки внешнего вида
 * @param navController - контроллер навигации (создается автоматически)
 * @param featureNavigations - список всех фич для регистрации
 * 
 * ИСПОЛЬЗОВАНИЕ В MainActivity:
 * ```kotlin
 * AppNavigation(
 *     modifier = Modifier.fillMaxSize(),
 *     featureNavigations = NavigationFactory.getFeatureNavigations()
 * )
 * ```
 * 
 * КАК ДОБАВИТЬ НОВУЮ ФИЧУ:
 * 1. Создайте класс, реализующий FeatureNavigation
 * 2. Добавьте экземпляр в NavigationFactory.getFeatureNavigations()
 * 3. Фича автоматически зарегистрируется в MainNavigationScreen
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    featureNavigations: List<FeatureNavigation>
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.MAIN,
        modifier = modifier
    ) {
        // ============================================================================
        // ГЛАВНЫЙ ЭКРАН С BOTTOM NAVIGATION
        // ============================================================================
        
        /**
         * Главный экран содержит:
         * - Bottom navigation для переключения между фичами
         * - Вложенный NavHost для экранов фич
         * - Bottom navigation остается видимым на всех экранах фич
         */
        composable(NavigationRoutes.MAIN) {
            MainNavigationScreen(
                navController = navController,
                featureNavigations = featureNavigations
            )
        }
    }
} 