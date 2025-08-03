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

    fun createFeatureRoute(parentRoute: String, featureName: String): String =
        if (parentRoute.isEmpty()) featureName else "$parentRoute/$featureName"

    fun createParametrizedRoute(baseRoute: String, vararg parameters: String): String =
        if (parameters.isEmpty()) {
            baseRoute
        } else {
            baseRoute + parameters.joinToString("/") { "{$it}" }
        }

    fun createPath(baseRoute: String, vararg parameterValues: String): String =
        if (parameterValues.isEmpty()) {
            baseRoute
        } else {
            baseRoute + parameterValues.joinToString("/")
        }

    object StubFeature {
        const val FEATURE_NAME = "stub"
    }

    object HomeFeature {
        const val FEATURE_NAME = "home"
    }

    object ProfileFeature {
        const val FEATURE_NAME = "profile"
    }

    object QuestionsFeature {
        const val FEATURE_NAME = "questions"
    }

    object DetailsFeature {
        const val FEATURE_NAME = "details"
    }

    object DetailQuestionFeature {
        const val FEATURE_NAME = "detail_question"
    }
}