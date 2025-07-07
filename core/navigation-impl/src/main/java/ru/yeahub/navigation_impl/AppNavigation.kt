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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.getKoin
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
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
 * Логика навигации в нижней панели:
 * - Если уже на родительском экране таба - ничего не делаем (избегаем пересоздания)
 * - Если в подэкране текущего таба - возвращаемся на родительский экран таба
 * - Если в другом табе - переходим на родительский экран выбранного таба
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
                            // Если уже на родительском маршруте этого таба, ничего не делаем
                            if (currentRoute == item.route) {
                                return@NavigationBarItem
                            }

                            // Если мы в подмаршруте этого таба, навигируем на родительский
                            if (currentRoute != null && currentRoute.startsWith(item.route)) {
                                Log.d(
                                    "NavDebug",
                                    "Navigating to parent route: ${item.route} from child: $currentRoute"
                                )
                                navController.navigate(item.route) {
                                    popUpTo(item.route) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            } else {
                                // Навигируем на родительский маршрут другого таба
                                Log.d(
                                    "NavDebug",
                                    "Navigating to different tab: ${item.route} from: $currentRoute"
                                )
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
                startDestination = if (features.isNotEmpty()) {
                    navItems.first().route
                } else {
                    FeatureRoute.StubFeature.FEATURE_NAME
                },
                modifier = Modifier,
//                анимация
//                enterTransition = { EnterTransition.None },
//                exitTransition = { ExitTransition.None },
//                popEnterTransition = { EnterTransition.None },
//                popExitTransition = { ExitTransition.None }
            ) {
                val rootFeatures = features.filter { it.isRootFeature() }
                val childFeatures = features.filter { !it.isRootFeature() }
                
                // Регистрируем корневые фичи
                rootFeatures.forEach { feature ->
                    Log.d("NavDebug", "Registering root feature: ${feature.javaClass.simpleName}")
                    feature.registerGraph(this, navController, "")
                }
                
                // Регистрируем дочерние фичи для всех корневых фич
                childFeatures.forEach { childFeature ->
                    val dependentRootFeatures = childFeature.getDependentRootFeatures(rootFeatures)
                    
                    // Если фича не указала зависимости, регистрируем для всех корневых фич
                    val targetRootFeatures = if (dependentRootFeatures.isEmpty()) {
                        rootFeatures
                    } else {
                        dependentRootFeatures
                    }
                    targetRootFeatures.forEach { rootFeature ->
                        Log.d(
                            "NavDebug",
                            "Registering ${childFeature.javaClass.simpleName} for ${rootFeature.javaClass.simpleName}"
                        )
                        childFeature.registerGraph(this, navController, rootFeature.getFeatureName())
                    }
                }
                //так как написано внизу не внедряем фичи это чисто для стаба + Debug
                composable(FeatureRoute.StubFeature.FEATURE_NAME) {
                    StubScreen()
                }
            }
        }
    }
} 