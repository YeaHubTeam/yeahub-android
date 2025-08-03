package ru.yeahub.example_home.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.example_home.api.HomeScreenApi
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

class HomeFeatureImpl(private val homeScreen: HomeScreenApi) : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.HomeFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = true  // Корневая фича

    override fun initialize(pathManager: NavigationPathManager) {
        super.initialize(pathManager)
        Timber.d("HomeFeatureImpl initialize: Registering home feature paths")

        // Регистрируем домашний маршрут как корневой
        pathManager.registerFeaturePath(getFeatureName(), getFeatureName())
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val currentPath = pathManager.getCurrentPath()
        Timber.d("HomeFeatureImpl registerGraph: currentPath: $currentPath")

        // Корневая фича регистрирует только свой корневой маршрут
        val currentHomeRoute = if (currentPath.isEmpty()) {
            getFeatureName()
        } else {
            pathManager.createChildPath(getFeatureName())
        }

        Timber.d("HomeFeatureImpl registerGraph: Registering route: $currentHomeRoute")

        navGraphBuilder.composable(currentHomeRoute) {
            homeScreen.HomeScreen(
                onProfileClick = { userId, userName ->
                    handleProfileNavigation(pathManager, navController, userId, userName)
                },
                onQuestionClick = {
                    handleQuestionsNavigation(pathManager, navController)
                },
                onDetailsClick = { itemId, title ->
                    handleDetailsNavigation(pathManager, navController, itemId, title)
                }
            )
        }
    }

    /**
     * Обработка навигации к профилю.
     */
    private fun handleProfileNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
        userId: String,
        userName: String
    ) {
        // Сбрасываем текущий путь на корневую фичу
        pathManager.setCurrentPath(getFeatureName())

        val profilePath = pathManager.createParametrizedPath(
            featureName = FeatureRoute.ProfileFeature.FEATURE_NAME,
            "userId",
            "userName"
        )

        val concretePath = pathManager.createConcretePath(
            profilePath,
            userId,
            userName
        )

        Timber.d("HomeFeatureImpl handleProfileNavigation: Navigating to: $concretePath")

        pathManager.setCurrentPath(concretePath)
        navController.navigate(concretePath)
    }

    /**
     * Обработка навигации к вопросам.
     */
    private fun handleQuestionsNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController
    ) {
        // Сбрасываем текущий путь на корневую фичу
        pathManager.setCurrentPath(getFeatureName())

        val questionsPath = pathManager.createChildPath(FeatureRoute.QuestionsFeature.FEATURE_NAME)

        Timber.d("HomeFeatureImpl handleQuestionsNavigation: Navigating to: $questionsPath")

        pathManager.setCurrentPath(questionsPath)
        navController.navigate(questionsPath)
    }

    /**
     * Обработка навигации к деталям.
     */
    private fun handleDetailsNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
        itemId: String,
        title: String
    ) {
        // Сбрасываем текущий путь на корневую фичу
        pathManager.setCurrentPath(getFeatureName())

        val detailsPath = pathManager.createParametrizedPath(
            featureName = FeatureRoute.DetailsFeature.FEATURE_NAME,
            "itemId",
            "title"
        )

        val concretePath = pathManager.createConcretePath(
            detailsPath,
            itemId,
            title
        )

        Timber.d("HomeFeatureImpl handleDetailsNavigation: Navigating to: $concretePath")

        pathManager.setCurrentPath(concretePath)
        navController.navigate(concretePath)
    }
}
