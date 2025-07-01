package ru.yeahub.navigation_impl

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.compose.koinInject
import ru.yeahub.example_home.api.HomeScreenApi
import ru.yeahub.example_home.api.navigation.HomeRoutes
import ru.yeahub.example_profile.api.ProfileScreenApi
import ru.yeahub.example_profile.api.navigation.ProfileRoutes
import ru.yeahub.navigation_impl.features.StubScreen
import ru.yeahub.navigation_impl.features.navigation.StubRoutes
import ru.yeahub.navigation_impl.model.BottomNavigationItem

/**
 * Основной компонент навигации приложения.
 * 
 * Архитектура навигации:
 * - Использует single-activity подход с Jetpack Navigation
 * - Поддерживает нижнюю навигацию для основных разделов
 * - Каждая фича-модуль определяет свои собственные маршруты в api модуле
 * 
 * Структура навигации:
 * 1. Нижняя панель навигации для переключения между основными разделами
 * 2. NavHost для управления навигационным стеком
 * 3. Отдельные composable для каждого экрана
 * 
 * @param modifier Модификатор для настройки внешнего вида
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navItems = NavigationFactory.getBottomNavItems()
    var selectedRoute by remember { mutableStateOf(navItems.first().route) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = selectedRoute == item.route,
                        onClick = {
                            selectedRoute = item.route
                            // Настройка навигации для нижней панели:
                            // - popUpTo удаляет все экраны до стартового
                            // - launchSingleTop предотвращает создание дубликатов экрана
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        label = { Text(item.label) },
                        icon = { Icon(item.icon, contentDescription = item.label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavHost(
                navController = navController,
                startDestination = HomeRoutes.HOME
            ) {
                // Главный экран
                // Демонстрирует простой переход с передачей параметров
                composable(HomeRoutes.HOME) {
                    val homeScreenApi = koinInject<HomeScreenApi>()
                    homeScreenApi.HomeScreen(
                        onProfileClick = { userId, userName ->
                            navController.navigate(
                                ProfileRoutes.profileRoute(userId, userName)
                            )
                        }
                    )
                }

                // Экран профиля
                // Демонстрирует:
                // 1. Получение параметров через navArgument
                // 2. Безопасную обработку null-параметров
                // 3. Навигацию назад через navigateUp
                composable(
                    route = ProfileRoutes.PROFILE,
                    arguments = listOf(
                        navArgument("userId") { type = NavType.StringType },
                        navArgument("userName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId") ?: ""
                    val userName = backStackEntry.arguments?.getString("userName") ?: ""
                    val profileScreenApi = koinInject<ProfileScreenApi>()
                    profileScreenApi.ProfileScreen(
                        userId = userId,
                        userName = userName,
                        onBackClick = {
                            navController.navigateUp()
                        }
                    )
                }

                // Экран-заглушка
                // Используется как пример для демонстрации нижней навигации
                composable(StubRoutes.STUB) {
                    StubScreen()
                }
            }
        }
    }
} 