package ru.yeahub.navigation_impl

import ru.yeahub.navigation_impl.model.BottomNavigationItem

/**
 * Фабрика для создания навигационных элементов.
 *
 * Этот объект отвечает за:
 * 1. Создание элементов нижней навигации
 * 2. Централизованное управление навигационными элементами
 * 3. Поддержку расширяемости навигации
 *
 * Как добавить новый элемент в нижнюю навигацию:
 * 1. Создайте Routes объект в api модуле вашей фичи
 * 2. Добавьте новый объект в BottomNavigationItem
 * 3. Добавьте элемент в список navItems здесь
 * 4. Зарегистрируйте composable в AppNavigation
 *
 * Возвращает список элементов для нижней навигации.
 * Порядок элементов в списке определяет их порядок
 * отображения в нижней панели навигации.
 * @return Список элементов нижней навигации
 */
fun getBottomNavItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem.Home,
        BottomNavigationItem.Stub
    )
}

// Функция для определения какая кнопка должна быть выделена
fun getSelectedRoute(currentRoute: String?, navItems: List<BottomNavigationItem>): String {
    val route = when {
        currentRoute == null -> navItems.first().route
        navItems.any { it.route == currentRoute } -> currentRoute
        else -> navItems.find { currentRoute.startsWith(it.route) }?.route
            // для дебага используется last()
            ?: navItems.last().route
    }
    return route
}