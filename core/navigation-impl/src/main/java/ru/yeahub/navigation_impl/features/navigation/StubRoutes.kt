package ru.yeahub.navigation_impl.features.navigation

import ru.yeahub.navigation_api.FeatureRoute

/**
 * Маршруты для Stub фичи.
 * Внутренняя реализация для navigation-impl модуля.
 */
object StubRoutes : FeatureRoute {
    private const val BASE_ROUTE = "stub"
    override val baseRoute: String = BASE_ROUTE

    // Основной экран
    const val STUB = BASE_ROUTE
} 