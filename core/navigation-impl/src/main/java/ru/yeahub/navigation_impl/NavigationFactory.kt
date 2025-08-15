package ru.yeahub.navigation_impl

import ru.yeahub.navigation_impl.model.BottomNavigationItem

/**
 * Фабрика для создания навигационных элементов.
 */
fun getBottomNavItems(): List<BottomNavigationItem> = listOf(
    BottomNavigationItem.Collections,
    BottomNavigationItem.Home,
    BottomNavigationItem.Questions
)

fun getSelectedRoute(currentRoute: String?, navItems: List<BottomNavigationItem>): String = when {
    currentRoute == null -> navItems[1].route
    navItems.any { it.route == currentRoute } -> currentRoute
    else -> navItems.find { currentRoute.startsWith(it.route) }?.route ?: navItems.last().route
}