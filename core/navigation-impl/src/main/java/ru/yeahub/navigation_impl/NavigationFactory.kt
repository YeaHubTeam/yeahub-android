package ru.yeahub.navigation_impl

import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_impl.model.BottomNavigationItem

/**
 * Фабрика для создания навигационных элементов.
 */
fun getBottomNavItems(): List<BottomNavigationItem> = listOf(
    BottomNavigationItem.Profile,
    BottomNavigationItem.Collections,
    BottomNavigationItem.Home,
    BottomNavigationItem.Questions
)

fun getStartDestination(isAuthorized: Boolean): String = if (isAuthorized) {
    FeatureRoute.HomeFeature.FEATURE_NAME
} else {
    FeatureRoute.AuthenticationFeature.LOGIN_ROUTE
}

fun getSelectedRoute(currentRoute: String?, navItems: List<BottomNavigationItem>): String = when {
    currentRoute == null -> FeatureRoute.HomeFeature.FEATURE_NAME
    currentRoute.startsWith(FeatureRoute.AuthenticationFeature.AUTH_ROUTE) ->
        BottomNavigationItem.Profile.route
    currentRoute == FeatureRoute.RegistrationFeature.FEATURE_NAME ->
        BottomNavigationItem.Profile.route
    navItems.any { it.route == currentRoute } -> currentRoute
    else -> navItems.find { currentRoute.startsWith(it.route) }?.route ?: navItems.last().route
}
