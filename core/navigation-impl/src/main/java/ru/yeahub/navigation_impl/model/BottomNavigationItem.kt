package ru.yeahub.navigation_impl.model

import androidx.annotation.DrawableRes
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_impl.R

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
    @DrawableRes val icon: Int
) {
    data object Collections : BottomNavigationItem(
        route = FeatureRoute.StubFeature.FEATURE_NAME,
        label = "Коллекции",
        icon = R.drawable.icon_tab_questions
    )

    data object Home : BottomNavigationItem(
        route = FeatureRoute.HomeFeature.FEATURE_NAME,
        label = "Главная",
        icon = R.drawable.icon_tab_home
    )

    data object Questions : BottomNavigationItem(
        route = FeatureRoute.QuestionsFeature.FEATURE_NAME,
        label = "Вопросы",
        icon =  R.drawable.icon_tab_collections
    )
} 