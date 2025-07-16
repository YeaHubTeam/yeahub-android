package ru.yeahub.navigation_impl.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.ui.graphics.vector.ImageVector
import ru.yeahub.navigation_api.FeatureRoute

/**
 * Модель для элементов нижней навигации.
 *
 * Структура:
 * - route: маршрут из соответствующего Routes объекта фичи
 * - label: отображаемый текст в нижней навигации
 * - icon: иконка из Material Icons
 *
 * Добавление нового элемента:
 * 1. Создайте Routes объект в impl модуле фичи
 * 2. Создайте новый data object здесь
 * 3. Добавьте элемент в NavigationFactory.getBottomNavItems()
 * 4. Добавьте composable в AppNavigation
 *
 * Пример добавления:
 * ```
 * data object NewScreen : BottomNavigationItem(
 *     route = NavigationRoutes.Bottom.NEW_SCREEN,
 *     label = "Новый экран",
 *     icon = Icons.Default.Add
 * )
 * ```
 */
sealed class BottomNavigationItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : BottomNavigationItem(
        route = FeatureRoute.HomeFeature.FEATURE_NAME,
        label = "Главная",
        icon = Icons.Default.Home
    )

    data object Stub : BottomNavigationItem(
        route = FeatureRoute.StubFeature.FEATURE_NAME,
        label = "Заглушка",
        icon = Icons.Default.QuestionMark
    )
} 