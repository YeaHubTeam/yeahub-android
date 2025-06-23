package com.example.navigation_impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigation_api.FeatureNavigation
import com.example.navigation_api.NavigationRoutes
import ru.yeahub.example_profile.api.ExampleProfileRoutes

/**
 * Главный экран с навигацией по фичам
 * 
 * АРХИТЕКТУРА:
 * Этот экран содержит bottom navigation и вложенный NavHost для экранов фич.
 * Каждая фича отображается как отдельная вкладка в bottom navigation.
 * 
 * СТРУКТУРА:
 * 1. Scaffold с bottom navigation
 * 2. Вложенный NavHost для экранов фич
 * 3. Bottom navigation остается видимым на всех экранах
 * 
 * ПАРАМЕТРЫ:
 * @param navController - главный контроллер навигации
 * @param featureNavigations - список всех фич
 * 
 * КАК НАСТРОИТЬ BOTTOM NAVIGATION:
 * 1. Добавьте иконки в getNavigationIcon()
 * 2. Добавьте лейблы в getNavigationLabel()
 * 3. Настройте цвета и стили в BottomNavigationBar
 * 
 * ПРИМЕЧАНИЕ:
 * Этот экран автоматически создается при переходе на маршрут MAIN
 */
@Composable
fun MainNavigationScreen(
    navController: NavController,
    featureNavigations: List<FeatureNavigation>
) {
    // Вложенный контроллер для bottom navigation
    val bottomNavController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = bottomNavController,
                featureNavigations = featureNavigations
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ============================================================================
            // ВЛОЖЕННЫЙ NAVHOST ДЛЯ ЭКРАНОВ ФИЧ
            // ============================================================================
            
            /**
             * Вложенный NavHost для экранов фич
             * 
             * ПРИНЦИП РАБОТЫ:
             * 1. Каждая фича отображается как отдельная вкладка
             * 2. При переключении вкладок меняется содержимое
             * 3. Bottom navigation остается видимым на всех экранах
             * 4. Каждая фича может иметь свою внутреннюю навигацию
             * 
             * СТАРТОВЫЙ ЭКРАН:
             * По умолчанию открывается первая фича (HOME)
             * Можно изменить, поменяв startDestination
             */
            NavHost(
                navController = bottomNavController,
                startDestination = NavigationRoutes.Home.BASE
            ) {
                // ============================================================================
                // РЕГИСТРАЦИЯ ЭКРАНОВ ФИЧ
                // ============================================================================
                
                /**
                 * Автоматическая регистрация экранов всех фич
                 * 
                 * ПРОЦЕСС:
                 * 1. Проходим по всем featureNavigations
                 * 2. Вызываем registerGraph() для каждой фичи
                 * 3. Фича регистрирует свои экраны в навигационном графе
                 * 
                 * ПРИМЕЧАНИЕ:
                 * Каждая фича создает свои экраны в вложенном графе
                 * Это обеспечивает изоляцию и независимость фич
                 */
                featureNavigations.forEach { featureNavigation ->
                    featureNavigation.registerGraph(this, bottomNavController)
                }
            }
        }
    }
}

/**
 * Bottom navigation bar
 * 
 * АРХИТЕКТУРА:
 * Создает bottom navigation с вкладками для всех фич.
 * Каждая фича представлена иконкой и лейблом.
 * 
 * ФУНКЦИОНАЛЬНОСТЬ:
 * - Отображение текущей активной вкладки
 * - Переключение между фичами
 * - Сохранение состояния при переключении
 * 
 * КАК НАСТРОИТЬ:
 * 1. Добавьте иконки в getNavigationIcon()
 * 2. Добавьте лейблы в getNavigationLabel()
 * 3. Настройте цвета и стили
 * 
 * ПАРАМЕТРЫ:
 * @param navController - контроллер для bottom navigation
 * @param featureNavigations - список фич для отображения
 */
@Composable
private fun BottomNavigationBar(
    navController: NavController,
    featureNavigations: List<FeatureNavigation>
) {
    // Получаем текущий экран для подсветки активной вкладки
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    NavigationBar {
        featureNavigations.forEach { featureNavigation ->
            val route = featureNavigation.getBaseRoute()
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(
                            id = getNavigationIcon(route)
                        ),
                        contentDescription = null
                    )
                },
                label = { Text(text = getNavigationLabel(route)) },
                selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                onClick = {
                    navController.navigate(route) {
                        // Очищаем стек до корня при выборе фичи
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

/**
 * Получение иконки для навигации
 * 
 * КАК ДОБАВИТЬ НОВУЮ ИКОНКУ:
 * 1. Добавьте case для нового маршрута
 * 2. Верните соответствующий ресурс иконки
 * 
 * ПРИМЕЧАНИЕ:
 * Сейчас используются системные иконки Android.
 * Для production рекомендуется использовать Material Icons или свои ресурсы.
 * 
 * ПАРАМЕТРЫ:
 * @param route - маршрут фичи
 * 
 * ВОЗВРАЩАЕТ:
 * ID ресурса иконки
 */
private fun getNavigationIcon(route: String): Int {
    return when (route) {
        NavigationRoutes.Home.BASE -> android.R.drawable.ic_menu_help
        ExampleProfileRoutes.BASE -> android.R.drawable.ic_menu_myplaces
        else -> android.R.drawable.ic_menu_view
    }
}

/**
 * Получение лейбла для навигации
 * 
 * КАК ДОБАВИТЬ НОВЫЙ ЛЕЙБЛ:
 * 1. Добавьте case для нового маршрута
 * 2. Верните соответствующий текст
 * 
 * ПРИМЕЧАНИЕ:
 * Лейблы отображаются под иконками в bottom navigation.
 * Используйте короткие и понятные названия.
 * 
 * ПАРАМЕТРЫ:
 * @param route - маршрут фичи
 * 
 * ВОЗВРАЩАЕТ:
 * Текст лейбла для отображения
 */
private fun getNavigationLabel(route: String): String {
    return when (route) {
        NavigationRoutes.Home.BASE -> "Главная"
        ExampleProfileRoutes.BASE -> "Профиль"
        else -> "Другое"
    }
} 