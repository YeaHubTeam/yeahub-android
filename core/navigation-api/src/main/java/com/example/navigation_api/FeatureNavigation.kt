package com.example.navigation_api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * Интерфейс для навигации фич
 * 
 * КАК РЕАЛИЗОВАТЬ НОВУЮ ФИЧУ:
 * 1. Создайте класс, реализующий этот интерфейс
 * 2. В registerGraph() определите навигационный граф фичи
 * 3. В getBaseRoute() верните базовый маршрут фичи
 * 4. Зарегистрируйте фичу в NavigationFactory
 * 
 * ПРИМЕР РЕАЛИЗАЦИИ:
 * ```kotlin
 * class MyFeatureNavigation : FeatureNavigation {
 *     override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
 *         navGraphBuilder.navigation(startDestination = "my_feature", route = "my_feature") {
 *             composable("my_feature") { MyFeatureScreen() }
 *             composable("my_feature/details/{id}") { backStackEntry ->
 *                 val id = backStackEntry.arguments?.getString("id")
 *                 MyFeatureDetailsScreen(id = id)
 *             }
 *         }
 *     }
 *     
 *     override fun getBaseRoute(): String = "my_feature"
 * }
 * ```
 * 
 * ВАЖНО:
 * - Каждая фича должна иметь уникальный базовый маршрут
 * - Используйте вложенные навигации для экранов фичи
 * - Добавьте маршруты в NavigationRoutes для централизованного управления
 */
interface FeatureNavigation {
    /**
     * Регистрирует навигационный граф фичи
     * 
     * ПАРАМЕТРЫ:
     * @param navGraphBuilder - билдер для создания навигационного графа
     * @param navController - контроллер навигации для переходов между экранами
     * 
     * ЧТО ДЕЛАТЬ В ЭТОМ МЕТОДЕ:
     * 1. Создайте вложенную навигацию с помощью navGraphBuilder.navigation()
     * 2. Определите все экраны фичи с помощью composable()
     * 3. Настройте переходы между экранами через navController
     * 
     * ПРИМЕР:
     * ```kotlin
     * navGraphBuilder.navigation(startDestination = "feature_main", route = "feature") {
     *     composable("feature_main") { FeatureMainScreen() }
     *     composable("feature_details/{id}") { backStackEntry ->
     *         val id = backStackEntry.arguments?.getString("id")
     *         FeatureDetailsScreen(id = id)
     *     }
     * }
     * ```
     */
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    )
    
    /**
     * Возвращает базовый маршрут фичи
     * 
     * ВОЗВРАЩАЕТ:
     * Строку с базовым маршрутом фичи (используется в bottom navigation)
     * 
     * ВАЖНО:
     * - Маршрут должен быть уникальным среди всех фич
     * - Этот маршрут будет использоваться в bottom navigation
     * - Добавьте константу в NavigationRoutes
     * 
     * ПРИМЕР:
     * ```kotlin
     * override fun getBaseRoute(): String = "my_feature"
     * ```
     */
    fun getBaseRoute(): String
} 