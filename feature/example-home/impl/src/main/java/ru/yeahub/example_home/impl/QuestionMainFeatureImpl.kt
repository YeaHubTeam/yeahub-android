package ru.yeahub.example_home.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.example_home.impl.presentation.view.QuestionsMainScreen
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

class QuestionMainFeatureImpl : FeatureApi {

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
        modifier: Modifier,
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
            QuestionsMainScreen(
                onBackClick = {},
                onNavigateToBaseQuestions = {
                    handleQuestionsNavigation(pathManager, navController)
                },
                onNavigateToCollections = {
                    handleCollectionsNavigation(pathManager, navController)
                },
                onNavigateToInterviewTrainer = {
                    handleInterviewTrainerNavigation(pathManager, navController)
                }
            )
        }
    }

    /**
     * Обработка навигации к вопросам.
     */
    private fun handleQuestionsNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
    ) {
        // Сбрасываем текущий путь на корневую фичу
        pathManager.setCurrentPath(FeatureRoute.QuestionsFeature.FEATURE_NAME)

        val questionsPath = pathManager.getCurrentPath()

        Timber.d("HomeFeatureImpl handleQuestionsNavigation: Navigating to: $questionsPath")

        navController.navigate(questionsPath) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    /**
     * Обработка навигации к коллекциям .
     */
    private fun handleCollectionsNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
    ) {
        // Сбрасываем текущий путь на корневую фичу
        pathManager.setCurrentPath(FeatureRoute.CollectionsFeature.FEATURE_NAME)

        val questionsPath = pathManager.getCurrentPath()

        Timber.d("HomeFeatureImpl handleQuestionsNavigation: Navigating to: $questionsPath")

        navController.navigate(questionsPath) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    /**
     * Обработка навигации к интервью тренажеру.
     */
    private fun handleInterviewTrainerNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
    ) {
        val titleTopAppBar = "Interview Trainer"

        // Сбрасываем текущий путь на корневую фичу
        pathManager.setCurrentPath(FeatureRoute.InterviewTrainerFeature.FEATURE_NAME)

        val createQuizPath = pathManager.getCurrentPath() + "/" +
                FeatureRoute.InterviewTrainerFeature.CREATE_QUIZ_SCREEN_NAME + "/" +
                titleTopAppBar

        Timber.d("HomeFeatureImpl handleInterviewTrainerNavigation: Navigating to: $createQuizPath")

        navController.navigate(createQuizPath) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
