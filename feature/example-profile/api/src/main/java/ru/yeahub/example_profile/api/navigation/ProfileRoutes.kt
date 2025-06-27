package ru.yeahub.example_profile.api.navigation

import ru.yeahub.navigation_api.FeatureRoute

/**
 * Маршруты для Profile фичи
 */
object ProfileRoutes : FeatureRoute {
    private const val BASE_ROUTE = "profile"
    override val baseRoute: String = BASE_ROUTE

    // Основной экран с параметрами
    const val PROFILE = "$BASE_ROUTE/{userId}/{userName}"
    
    // Вложенные экраны
    const val SETTINGS = "$BASE_ROUTE/settings"
    const val EDIT = "$BASE_ROUTE/edit"
    
    // Вспомогательные функции для создания маршрутов с параметрами
    fun profileRoute(userId: String, userName: String) = "$BASE_ROUTE/$userId/$userName"
} 