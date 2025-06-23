package ru.yeahub.example_profile.api

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * API интерфейс для фичи Profile
 * 
 * АРХИТЕКТУРА:
 * Этот интерфейс определяет контракт для фичи Profile.
 * Он содержит методы для регистрации экранов и получения базового маршрута.
 * 
 * ПРИНЦИП РАБОТЫ:
 * 1. Определяет методы для регистрации экранов фичи
 * 2. Предоставляет базовый маршрут для bottom navigation
 * 3. Позволяет фиче быть независимой от навигации
 * 
 * ИСПОЛЬЗОВАНИЕ:
 * Реализуйте этот интерфейс в impl модуле:
 * ```kotlin
 * class ExampleProfileImpl : ExampleProfileApi {
 *     override fun registerGraph(navGraphBuilder, navController) { ... }
 *     override fun getBaseRoute(): String = "profile"
 * }
 * ```
 */
interface ExampleProfileApi {
    
    /**
     * Регистрирует экраны фичи в навигационном графе
     * 
     * ПАРАМЕТРЫ:
     * @param navGraphBuilder - билдер для создания навигационного графа
     * @param navController - контроллер навигации для переходов
     * 
     * ПРИМЕЧАНИЕ:
     * Этот метод вызывается автоматически при регистрации фичи.
     * Не вызывайте его вручную.
     */
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    )
    
    /**
     * Возвращает базовый маршрут фичи
     * 
     * ВОЗВРАЩАЕТ:
     * Строку с базовым маршрутом для bottom navigation
     * 
     * ПРИМЕЧАНИЕ:
     * Этот маршрут используется для:
     * - Регистрации в bottom navigation
     * - Определения стартового экрана фичи
     * - Навигации между фичами
     */
    fun getBaseRoute(): String
} 