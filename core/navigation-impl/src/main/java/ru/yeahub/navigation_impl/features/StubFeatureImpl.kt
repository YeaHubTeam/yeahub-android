package ru.yeahub.navigation_impl.features

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

/**
 * Реализация stub фичи для отладки и демонстрации.
 *
 * Демонстрирует:
 * - Регистрацию фичи как корневой
 * - Навигацию к дочерним фичам (Details)
 * - Интеграцию с NavigationPathManager
 */
class StubFeatureImpl : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.StubFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = true  // Stub как корневая фича для демонстрации

    override fun initialize(pathManager: NavigationPathManager) {
        super.initialize(pathManager)
        Timber.d("StubFeatureImpl initialize: Registering stub feature")
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val currentPath = pathManager.getCurrentPath()
        Timber.d("StubFeatureImpl registerGraph: currentPath: $currentPath")

        // Создаем маршрут для stub экрана
        val stubRoute = if (currentPath.isEmpty()) {
            getFeatureName()
        } else {
            pathManager.createChildPath(getFeatureName())
        }

        Timber.d("StubFeatureImpl registerGraph: Registering route: $stubRoute")

        navGraphBuilder.composable(stubRoute) {
            StubScreen(
                onDetailsClick = { itemId, title ->
                    handleDetailsNavigation(pathManager, navController, itemId, title)
                }
            )
        }
    }

    /**
     * Обработка навигации к Details экрану из Stub экрана.
     */
    private fun handleDetailsNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
        itemId: String,
        title: String
    ) {
        // Используем stub как базовый путь для Details фичи
        pathManager.setCurrentPath(getFeatureName())

        val detailsPath = pathManager.createParametrizedPath(
            featureName = "details",
            "itemId",
            "title"
        )

        val concretePath = pathManager.createConcretePath(
            detailsPath,
            itemId,
            title
        )

        Timber.d("StubFeatureImpl handleDetailsNavigation: Navigating to: $concretePath")

        pathManager.setCurrentPath(concretePath)
        navController.navigate(concretePath)
    }
} 