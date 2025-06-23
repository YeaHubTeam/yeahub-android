package com.example.navigation_api

import androidx.navigation.NavHostController

/**
 * Основной интерфейс для навигации в приложении
 * 
 * ИСПОЛЬЗОВАНИЕ:
 * Этот интерфейс определяет контракт для навигации в приложении.
 * Реализация находится в navigation-impl модуле.
 * 
 * ВАЖНО:
 * - Не создавайте экземпляры напрямую
 * - Используйте NavigationFactory.createNavigation() для получения экземпляра
 * - Этот интерфейс обеспечивает слабую связанность между модулями
 * 
 * ПРИМЕР ИСПОЛЬЗОВАНИЯ:
 * ```kotlin
 * val navigationApi = NavigationFactory.createNavigation()
 * val startDestination = navigationApi.getStartDestination()
 * ```
 */
interface NavigationApi {
    /**
     * Регистрирует навигационные графы для фич
     * 
     * ПАРАМЕТРЫ:
     * @param navController - контроллер навигации для регистрации графов
     * 
     * ПРИМЕЧАНИЕ:
     * Этот метод вызывается автоматически при инициализации навигации.
     * Не вызывайте его вручную.
     */
    fun registerFeatureGraphs(navController: NavHostController)
    
    /**
     * Возвращает стартовый маршрут приложения
     * 
     * ВОЗВРАЩАЕТ:
     * Строку с маршрутом стартового экрана
     * 
     * ПРИМЕР:
     * ```kotlin
     * val startRoute = navigationApi.getStartDestination() // "main"
     * ```
     */
    fun getStartDestination(): String
} 