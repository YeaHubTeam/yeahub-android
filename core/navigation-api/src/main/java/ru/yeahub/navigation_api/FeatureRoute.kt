package ru.yeahub.navigation_api

/**
 * Базовый интерфейс для маршрутов фичи.
 * Каждая фича должна реализовать этот интерфейс в своем impl модуле.
 */
object FeatureRoute {

    object StubFeature {
        private const val BASE_ROUTE = "stub/"
        const val STUB = BASE_ROUTE
    }

    object HomeFeature {
        private const val BASE_ROUTE = "home/"
        const val HOME = BASE_ROUTE
    }

    class ProfileFeature(lastRoutes: String = "") {
        private val baseRoute = "${lastRoutes}profile/"

        // Основной экран с параметрами + родительский маршрут для переиспользуемости
        val profile = "$baseRoute{userId}/{userName}"

        // Вспомогательные функции для создания маршрутов с параметрами
        fun profileRoute(userId: String, userName: String) =
            "$baseRoute$userId/$userName"
    }
}