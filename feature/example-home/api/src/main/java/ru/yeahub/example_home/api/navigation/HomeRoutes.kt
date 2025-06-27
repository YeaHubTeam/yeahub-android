package ru.yeahub.example_home.api.navigation

import ru.yeahub.navigation_api.FeatureRoute

/**
 * Определение маршрутов для фичи Home.
 * 
 * Маршруты определяются в api модуле, чтобы:
 * 1. Другие модули могли использовать их для навигации
 * 2. Избежать циклических зависимостей
 * 3. Централизовать управление навигацией
 * 
 * Правила именования маршрутов:
 * - Используйте константы для предотвращения опечаток
 * - Маршруты должны быть уникальными в пределах всего приложения
 * - Для параметризованных маршрутов создавайте вспомогательные функции
 */
object HomeRoutes : FeatureRoute {
    private const val BASE_ROUTE = "home"
    override val baseRoute: String = BASE_ROUTE

    /**
     * Маршрут главного экрана.
     * Это простой маршрут без параметров, используемый как
     * стартовый экран приложения.
     */
    const val HOME = BASE_ROUTE
    
    // Вложенные экраны
    const val DETAILS = "$BASE_ROUTE/details"
    
    // Вспомогательные функции для создания маршрутов с параметрами
    fun detailsRoute(itemId: String) = "$BASE_ROUTE/details/$itemId"
} 