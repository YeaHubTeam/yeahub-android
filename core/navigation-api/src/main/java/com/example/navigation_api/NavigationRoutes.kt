package com.example.navigation_api

/**
 * Константы маршрутов навигации
 * 
 * КАК УПРАВЛЯТЬ МАРШРУТАМИ:
 * 1. Добавляйте новые маршруты в соответствующие объекты
 * 2. Используйте константы вместо строковых литералов
 * 3. Группируйте маршруты по фичам
 * 4. Следите за уникальностью маршрутов
 * 
 * СТРУКТУРА:
 * - Основные маршруты (MAIN, SPLASH) - для главной навигации
 * - Объекты фич (Home) - для bottom navigation и экранов фич
 * 
 * ПРИМЕР ДОБАВЛЕНИЯ НОВОЙ ФИЧИ:
 * ```kotlin
 * // Добавить объект с маршрутами фичи
 * object NewFeature {
 *     const val BASE = "new_feature"
 *     const val DETAILS = "new_feature/details"
 *     const val CREATE = "new_feature/create"
 * }
 * ```
 * 
 * ВАЖНО:
 * - Все маршруты должны быть уникальными
 * - Используйте константы вместо строковых литералов
 * - Группируйте маршруты логически
 * - Документируйте сложные маршруты
 */
object NavigationRoutes {
    // ============================================================================
    // ОСНОВНЫЕ МАРШРУТЫ
    // ============================================================================
    
    /**
     * Главный экран с bottom navigation
     */
    const val MAIN = "main"
    
    /**
     * Экран загрузки (splash screen)
     */
    const val SPLASH = "splash"
    
    // ============================================================================
    // ВЛОЖЕННЫЕ МАРШРУТЫ ФИЧ
    // ============================================================================
    
    /**
     * Маршруты фичи "Главная"
     * 
     * СТРУКТУРА:
     * - BASE - основной экран фичи (используется для bottom navigation)
     * - DETAILS - экран деталей
     */
    object Home {
        const val BASE = "home"
        const val DETAILS = "home/details"
    }

    
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
     * val route = NavigationRoutes.createRouteWithArg(
     *     baseRoute = Home.DETAILS,
     *     argName = "id",
     *     argValue = "123"
     * )
     * // Результат: "home/details/123"
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
     * val route = NavigationRoutes.createRouteWithArgs(
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