package ru.yeahub.navigation_api

/**
 * Утилитарный класс для работы с маршрутами фичи.
 *
 * Предоставляет функции для создания модульных маршрутов:
 * - Создание базовых маршрутов для фичи
 * - Создание вложенных маршрутов с параметрами
 * - Построение путей с учетом родительских маршрутов
 */
object FeatureRoute {
    /**
     * Создает базовый маршрут для фичи
     * @param parentRoute Родительский маршрут
     * @param featureName Название фичи
     * @return Полный маршрут фичи
     */
    fun createFeatureRoute(parentRoute: String, featureName: String): String {
        return if (parentRoute.isEmpty()) {
            featureName
        } else {
            "$parentRoute/$featureName"
        }
    }

    /**
     * Создает маршрут с параметрами
     * @param baseRoute Базовый маршрут
     * @param parameters Список параметров для маршрута
     * @return Маршрут с параметрами
     */
    fun createParametrizedRoute(baseRoute: String, vararg parameters: String): String {
        return if (parameters.isEmpty()) {
            baseRoute
        } else {
            baseRoute + parameters.joinToString("/") { "{$it}" }
        }
    }

    /**
     * Создает конкретный путь с заданными значениями параметров
     * @param baseRoute Базовый маршрут
     * @param parameterValues Значения параметров
     * @return Путь с конкретными значениями
     */
    fun createPath(baseRoute: String, vararg parameterValues: String): String {
        return if (parameterValues.isEmpty()) {
            baseRoute
        } else {
            baseRoute + parameterValues.joinToString("/")
        }
    }

    // Константы для основных фич (можно вынести в отдельные классы фич)
    object StubFeature {
        const val FEATURE_NAME = "stub"
    }

    object HomeFeature {
        const val FEATURE_NAME = "home"
    }

    object ProfileFeature {
        const val FEATURE_NAME = "profile"
        
        fun createProfileRoute(parentRoute: String): String {
            return createParametrizedRoute(
                createFeatureRoute(
                    parentRoute,
                    FEATURE_NAME
                ),
                "userId",
                "userName"
            )
        }
        
        fun createProfilePath(
            parentRoute: String,
            userId: String,
            userName: String
        ): String {
            return createPath(
                createFeatureRoute(
                    parentRoute,
                    FEATURE_NAME
                ),
                userId,
                userName
            )
        }
    }

    object QuestionsFeature {
        const val FEATURE_NAME = "questions"

        fun createQuestionsRoute(parentRoute: String): String {
            return createFeatureRoute(parentRoute, FEATURE_NAME)
        }
        
        fun createQuestionsPath(parentRoute: String): String {
            return createFeatureRoute(parentRoute, FEATURE_NAME)
        }
    }
}