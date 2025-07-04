package ru.yeahub.navigation_impl

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.getKoin
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import androidx.navigation.navArgument
import com.example.api.QuestionsScreenApi
import com.example.api.navigation.QuestionsRoutes
import org.koin.compose.koinInject
import ru.yeahub.example_home.api.HomeScreenApi
import ru.yeahub.example_home.api.navigation.HomeRoutes
import ru.yeahub.example_profile.api.ProfileScreenApi
import ru.yeahub.example_profile.api.navigation.ProfileRoutes
import ru.yeahub.navigation_impl.features.StubScreen

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
    modifier: Modifier = Modifier,
) {
    val features: Set<FeatureApi> = getKoin().getAll<FeatureApi>().toSet()
    Log.d("NavDebug", "Loaded features: ${features.map { it.javaClass.simpleName }}")
    val navController = rememberNavController()
    val navItems = getBottomNavItems()
    Log.d("NavDebug", "NavItems: ${navItems.map { "${it.label} -> ${it.route}" }}")

    // Отслеживаем текущий маршрут из NavController
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("NavDebug", "Current route: $currentRoute")
    val selectedRoute = getSelectedRoute(currentRoute, navItems)
    Log.d("NavDebug", "Selected route for bottom nav: $selectedRoute")

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = selectedRoute == item.route,
                        onClick = {
                            // Проверяем, не находимся ли мы уже на этом экране
                            if (selectedRoute != item.route) {
                                // Настройка навигации для нижней панели:
                                // - popUpTo удаляет все экраны до стартового
                                // - launchSingleTop предотвращает создание дубликатов экрана
                                // - saveState/restoreState сохраняют состояние табов
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        label = { Text(item.label) },
                        icon = { Icon(item.icon, contentDescription = item.label) }
                    )
                    Log.d("NavSelected", "$currentRoute")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavHost(
                navController = navController,
                startDestination = if (features.isNotEmpty()) navItems.first().route else FeatureRoute.StubFeature.STUB,
                modifier = Modifier,
//                анимация
//                enterTransition = { EnterTransition.None },
//                exitTransition = { ExitTransition.None },
//                popEnterTransition = { EnterTransition.None },
//                popExitTransition = { ExitTransition.None }
            ) {
                features.forEach { feature ->
                    Log.d("NavDebug", "Registering feature: ${feature.javaClass.simpleName}")
                    feature.registerGraph(this, navController)
                }
                composable(FeatureRoute.StubFeature.STUB) {
                    StubScreen()
                }

                composable(QuestionsRoutes.QUESTIONS) {
                    val questionScreenApi = koinInject<QuestionsScreenApi>()
                    questionScreenApi.QuestionsScreen(onBackClick = {
                        navController.navigateUp()
                    })
                }
            }
        }
    }
} 