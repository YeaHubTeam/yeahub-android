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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.getKoin
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import ru.yeahub.navigation_impl.model.BottomNavigationItem
import timber.log.Timber

/**
 * Основной компонент навигации приложения с динамическим управлением путями.
 *
 * Архитектура навигации:
 * - Использует single-activity подход с Jetpack Navigation
 * - Поддерживает нижнюю навигацию для основных разделов
 * - Каждая фича-модуль определяет свои собственные маршруты в api модуле
 * - Динамически управляет путями через NavigationPathManager
 *
 * Структура навигации:
 * 1. Нижняя панель навигации для переключения между основными разделами
 * 2. NavHost для управления навигационным стеком
 * 3. Отдельные composable для каждого экрана
 * 4. NavigationPathManager для динамического управления путями
 *
 * Логика навигации в нижней панели:
 * - Если уже на родительском экране таба - ничего не делаем (избегаем пересоздания)
 * - Если в подэкране текущего таба - возвращаемся на родительский экран таба
 * - Если в другом табе - переходим на родительский экран выбранного таба
 *
 * @param modifier Модификатор для настройки внешнего вида
 * @param pathManager Менеджер путей для динамического управления навигацией
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    pathManager: NavigationPathManager = getKoin().get<NavigationPathManager>(),
) {
    val features: Set<FeatureApi> = getKoin().getAll<FeatureApi>().toSet()
    Timber.d("AppNavigation onCreate: Loaded features: ${features.map { it.javaClass.simpleName }}")
    val navItems = getBottomNavItems()
    
    features.forEach { feature ->
        feature.initialize(pathManager)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedRoute = getSelectedRoute(currentRoute, navItems)
    
    currentRoute?.let { route ->
        pathManager.setCurrentPath(route)
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = selectedRoute == item.route,
                        onClick = {
                            handleBottomNavClick(
                                item = item,
                                currentRoute = currentRoute,
                                navController = navController,
                                pathManager = pathManager
                            )
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
            ) {
                registerDynamicNavigation(
                    features = features,
                    pathManager = pathManager,
                    navController = navController,
                    navGraphBuilder = this
                )
            }
        }
    }
}

/**
 * Обработка нажатий на нижнюю панель навигации.
 */
private fun handleBottomNavClick(
    item: BottomNavigationItem,
    currentRoute: String?,
    navController: NavHostController,
    pathManager: NavigationPathManager
) {
    // Если уже на родительском маршруте этого таба, ничего не делаем
    if (currentRoute == item.route) {
        return
    }

    // Если мы в подмаршруте этого таба, навигируем на родительский
    if (currentRoute != null && currentRoute.startsWith(item.route)) {
        pathManager.setCurrentPath(item.route)
        navController.navigate(item.route) {
            popUpTo(item.route) { inclusive = true }
            launchSingleTop = true
        }
    } else {
        // Навигируем на родительский маршрут другого таба
        Timber.d(
            "AppNavigation onClick: Navigating to different tab: " +
                "${item.route} from: $currentRoute"
        )
        
        // Устанавливаем новый корневой путь
        pathManager.setCurrentPath(item.route)
        navController.navigate(item.route) {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}

/**
 * Регистрация динамической навигации для всех фич.
 */
private fun registerDynamicNavigation(
    features: Set<FeatureApi>,
    pathManager: NavigationPathManager,
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    val rootFeatures = features.filter { it.isRootFeature() }
    val childFeatures = features.filter { !it.isRootFeature() }
    
    // Регистрируем корневые фичи
    rootFeatures.forEach { feature ->
        Timber.d(
            "AppNavigation registerGraph: Registering root feature: " +
                    "${feature.javaClass.simpleName}"
        )
        
        // Сбрасываем путь для корневой фичи
        pathManager.setCurrentPath("")
        pathManager.registerFeaturePath(feature.getFeatureName(), feature.getFeatureName())
        
        feature.registerGraph(navGraphBuilder, navController, pathManager)
    }
    
    // Регистрируем дочерние фичи для каждой корневой фичи
    registerChildFeatures(childFeatures, rootFeatures, pathManager, navController, navGraphBuilder)
}

/**
 * Регистрация дочерних фич для каждой корневой фичи.
 */
private fun registerChildFeatures(
    childFeatures: List<FeatureApi>,
    rootFeatures: List<FeatureApi>,
    pathManager: NavigationPathManager,
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    childFeatures.forEach { childFeature ->
        val dependentRootFeatures = childFeature.getDependentRootFeatures(rootFeatures)
        
        // Если фича не указала зависимости, регистрируем для всех корневых фич
        val targetRootFeatures = if (dependentRootFeatures.isEmpty()) {
            rootFeatures
        } else {
            dependentRootFeatures
        }
        
        targetRootFeatures.forEach { rootFeature ->
            pathManager.setCurrentPath(rootFeature.getFeatureName())
            
            // Регистрируем дочернюю фичу
            childFeature.registerGraph(
                navGraphBuilder = navGraphBuilder,
                navController = navController,
                pathManager = pathManager
            )
        }
    }
}
 