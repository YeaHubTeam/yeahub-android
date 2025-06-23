package ru.yeahub.example_profile.api

/**
 * Маршруты для фичи Profile
 * 
 * АРХИТЕКТУРА:
 * Этот объект содержит все маршруты, связанные с фичей Profile.
 * Все маршруты должны быть уникальными в рамках приложения.
 * 
 * СТРУКТУРА:
 * - BASE - основной экран профиля (используется для bottom navigation)
 * - EDIT - редактирование профиля
 * - SETTINGS - настройки профиля
 * 
 * ПРИМЕР ДОБАВЛЕНИЯ НОВОГО ЭКРАНА:
 * ```kotlin
 * const val NEW_SCREEN = "profile/new_screen"
 * ```
 * 
 * ВАЖНО:
 * - Все маршруты должны быть уникальными
 * - Используйте константы вместо строковых литералов
 * - Группируйте маршруты логически
 * - Документируйте сложные маршруты
 */
object ExampleProfileRoutes {
    
    /**
     * Основной экран профиля
     * Используется для bottom navigation
     */
    const val BASE = "profile"
    
    /**
     * Экран редактирования профиля
     */
    const val EDIT = "profile/edit"
    
    /**
     * Экран настроек профиля
     */
    const val SETTINGS = "profile/settings"
    
    // ============================================================================
    // ПОЛЕЗНЫЕ МЕТОДЫ
    // ============================================================================
    
    /**
     * Создает маршрут с аргументом
     * 
     * ПАРАМЕТРЫ:
     * @param baseRoute - базовый маршрут
     * @param argName - имя аргумента
     * @param argValue - значение аргумента
     * 
     * ПРИМЕР:
     * ```kotlin
     * val route = ExampleProfileRoutes.createRouteWithArg(
     *     baseRoute = EDIT,
     *     argName = "id",
     *     argValue = "123"
     * )
     * // Результат: "profile/edit/123"
     * ```
     */
    fun createRouteWithArg(baseRoute: String, argName: String, argValue: String): String {
        return "$baseRoute/$argValue"
    }
    
    /**
     * Создает маршрут с несколькими аргументами
     * 
     * ПАРАМЕТРЫ:
     * @param baseRoute - базовый маршрут
     * @param args - карта аргументов
     * 
     * ПРИМЕР:
     * ```kotlin
     * val route = ExampleProfileRoutes.createRouteWithArgs(
     *     baseRoute = "details",
     *     args = mapOf("id" to "123", "type" to "user")
     * )
     * // Результат: "details?id=123&type=user"
     * ```
     */
    fun createRouteWithArgs(baseRoute: String, args: Map<String, String>): String {
        return if (args.isEmpty()) {
            baseRoute
        } else {
            val argsString = args.entries.joinToString("&") { "${it.key}=${it.value}" }
            "$baseRoute?$argsString"
        }
    }
} 