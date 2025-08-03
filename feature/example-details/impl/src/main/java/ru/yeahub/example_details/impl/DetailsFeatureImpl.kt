package ru.yeahub.example_details.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.example_details.api.DetailsScreenApi
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

/**
 * Реализация фичи Details с поддержкой динамической навигации.
 *
 * Демонстрирует:
 * - Поддержку DeepLink
 * - Автоматическое управление путями навигации
 */
class DetailsFeatureImpl(private val detailsScreen: DetailsScreenApi) : FeatureApi {

    override fun getFeatureName(): String = "details"

    override fun isRootFeature(): Boolean = false  // Вложенная фича

    override fun initialize(pathManager: NavigationPathManager) {
        super.initialize(pathManager)
        Timber.d("DetailsFeatureImpl initialize: Registering feature paths")

        // Дополнительная инициализация для Details фичи
        // Может включать регистрацию специфичных для фичи путей
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val currentPath = pathManager.getCurrentPath()
        Timber.d("DetailsFeatureImpl registerGraph: currentPath: $currentPath")

        // Создаем маршрут для экрана деталей с учетом текущего пути
        val detailsRoute = pathManager.createParametrizedPath(
            featureName = getFeatureName(),
            "itemId",
            "title"
        )

        Timber.d("DetailsFeatureImpl registerGraph: Registering route: $detailsRoute")

        navGraphBuilder.composable(detailsRoute) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: "unknown"
            val title = backStackEntry.arguments?.getString("title") ?: "Unknown Title"

            // Создаем отображаемый путь для пользователя
            val displayPath = createDisplayPath(pathManager, itemId)

            detailsScreen.DetailsScreen(
                itemId = itemId,
                title = title,
                currentPath = displayPath,
                onBackClick = {
                    handleBackNavigation(pathManager, navController)
                }
            )
        }
    }

    /**
     * Создает отображаемый путь для пользователя.
     */
    private fun createDisplayPath(pathManager: NavigationPathManager, itemId: String): String {
        val currentPath = pathManager.getCurrentPath()
        return if (currentPath.isEmpty()) {
            "${getFeatureName()}/$itemId"
        } else {
            "$currentPath/${getFeatureName()}/$itemId"
        }
    }

    /**
     * Обработка навигации назад.
     */
    private fun handleBackNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController
    ) {
        val parentPath = pathManager.getParentPath()

        Timber.d("DetailsFeatureImpl handleBackNavigation: Navigating to parent: $parentPath")

        pathManager.setCurrentPath(parentPath)

        if (parentPath.isEmpty()) {
            navController.navigateUp()
        } else {
            navController.navigate(parentPath) {
                popUpTo(parentPath) {
                    inclusive = true
                }
            }
        }
    }
} 