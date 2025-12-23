package ru.yeahub.navigation_impl

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.getKoin
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.navigation_api.FeatureApi
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
        modifier = modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                containerColor = Theme.colors.purple700
            ) {
                navItems.forEach { item ->
                    val isSelected by remember(selectedRoute) {
                        derivedStateOf { selectedRoute == item.route }
                    }
                    NavigationBarItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Theme.colors.purple700,
                            selectedTextColor = Theme.colors.purple700,
                            unselectedIconColor = Theme.colors.white900,
                            unselectedTextColor = Theme.colors.white900,
                            indicatorColor = Color.Transparent
                        ),
                        selected = isSelected,
                        onClick = {
                            handleBottomNavClick(
                                item = item,
                                currentRoute = currentRoute,
                                navController = navController,
                                pathManager = pathManager
                            )
                        },
                        icon = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = animateColorAsState(
                                            targetValue = if (isSelected) Theme.colors.white900 else Color.Transparent,
                                            animationSpec = tween(durationMillis = 80)
                                        ).value,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(item.icon),
                                    contentDescription = item.label
                                )
                                Text(
                                    text = item.label,
                                    style = Theme.typography.bodyAccent
                                )
                            }
                        },
                        alwaysShowLabel = false
                    )
                    Timber.d("NavSelected", "$currentRoute")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavHost(
                navController = navController,
                startDestination = navItems[1].route,
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
        val targetRootFeatures = dependentRootFeatures.ifEmpty {
            rootFeatures
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
 