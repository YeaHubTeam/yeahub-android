package com.example.navigation_api

import androidx.navigation.NavController

/**
 * Команды навигации для типизированной навигации
 * 
 * ИСПОЛЬЗОВАНИЕ:
 * Этот sealed класс предоставляет типизированные команды для навигации.
 * Используйте executeCommand() для выполнения команд.
 * 
 * ПРЕИМУЩЕСТВА:
 * - Типобезопасность
 * - Легкое тестирование
 * - Централизованное управление навигацией
 * - Возможность логирования и отладки
 * 
 * ПРИМЕР ИСПОЛЬЗОВАНИЯ:
 * ```kotlin
 * // Простая навигация
 * val command = NavigationCommand.Navigate("home")
 * navController.executeCommand(command)
 * 
 * // Навигация с аргументами
 * val commandWithArgs = NavigationCommand.NavigateWithArgs(
 *     route = "details",
 *     arguments = mapOf("id" to "123")
 * )
 * navController.executeCommand(commandWithArgs)
 * 
 * // Навигация назад
 * navController.executeCommand(NavigationCommand.NavigateBack)
 * ```
 */
sealed class NavigationCommand {
    /**
     * Навигация к экрану
     * 
     * ПАРАМЕТРЫ:
     * @param route - маршрут для перехода
     * 
     * ПРИМЕР:
     * ```kotlin
     * NavigationCommand.Navigate("home")
     * ```
     */
    data class Navigate(val route: String) : NavigationCommand()
    
    /**
     * Навигация к экрану с аргументами
     * 
     * ПАРАМЕТРЫ:
     * @param route - маршрут для перехода
     * @param arguments - карта аргументов (ключ-значение)
     * 
     * ПРИМЕЧАНИЕ:
     * Аргументы передаются как query параметры в URL
     * 
     * ПРИМЕР:
     * ```kotlin
     * NavigationCommand.NavigateWithArgs(
     *     route = "details",
     *     arguments = mapOf("id" to "123", "type" to "user")
     * )
     * // Результат: "details?id=123&type=user"
     * ```
     */
    data class NavigateWithArgs(
        val route: String,
        val arguments: Map<String, String> = emptyMap()
    ) : NavigationCommand()
    
    /**
     * Навигация назад
     * 
     * ПРИМЕР:
     * ```kotlin
     * NavigationCommand.NavigateBack
     * ```
     */
    object NavigateBack : NavigationCommand()
    
    /**
     * Навигация назад с результатом
     * 
     * ПАРАМЕТРЫ:
     * @param key - ключ для сохранения результата
     * @param result - результат для передачи назад
     * 
     * ПРИМЕЧАНИЕ:
     * Результат сохраняется в SavedStateHandle предыдущего экрана
     * 
     * ПРИМЕР:
     * ```kotlin
     * NavigationCommand.NavigateBackWithResult(
     *     key = "selected_item",
     *     result = "item_123"
     * )
     * ```
     */
    data class NavigateBackWithResult(
        val key: String,
        val result: String
    ) : NavigationCommand()
    
    /**
     * Очистка стека навигации и переход к экрану
     * 
     * ПАРАМЕТРЫ:
     * @param route - маршрут для перехода
     * 
     * ПРИМЕЧАНИЕ:
     * Очищает весь стек навигации и переходит к указанному экрану
     * Полезно для logout или сброса состояния приложения
     * 
     * ПРИМЕР:
     * ```kotlin
     * NavigationCommand.NavigateAndClearStack("login")
     * ```
     */
    data class NavigateAndClearStack(val route: String) : NavigationCommand()
}

/**
 * Расширение для выполнения команд навигации
 * 
 * ИСПОЛЬЗОВАНИЕ:
 * Вызывайте этот метод для выполнения команд навигации
 * 
 * ПРИМЕР:
 * ```kotlin
 * val command = NavigationCommand.Navigate("home")
 * navController.executeCommand(command)
 * ```
 * 
 * ПРИМЕЧАНИЯ:
 * - Метод обрабатывает все типы команд навигации
 * - Для NavigateWithArgs аргументы передаются как query параметры
 * - Для NavigateBackWithResult результат сохраняется в SavedStateHandle
 * - Для NavigateAndClearStack очищается весь стек навигации
 */
fun NavController.executeCommand(command: NavigationCommand) {
    when (command) {
        is NavigationCommand.Navigate -> {
            navigate(command.route)
        }
        is NavigationCommand.NavigateWithArgs -> {
            // Создаем route с аргументами
            val routeWithArgs = if (command.arguments.isEmpty()) {
                command.route
            } else {
                val argsString = command.arguments.entries
                    .joinToString("&") { "${it.key}=${it.value}" }
                "${command.route}?$argsString"
            }
            navigate(routeWithArgs)
        }
        NavigationCommand.NavigateBack -> {
            popBackStack()
        }
        is NavigationCommand.NavigateBackWithResult -> {
            previousBackStackEntry?.savedStateHandle?.set(command.key, command.result)
            popBackStack()
        }
        is NavigationCommand.NavigateAndClearStack -> {
            navigate(command.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
} 