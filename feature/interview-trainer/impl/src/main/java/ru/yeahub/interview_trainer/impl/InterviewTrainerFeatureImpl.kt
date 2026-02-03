package ru.yeahub.interview_trainer.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.interview_trainer.api.InterviewTrainerApi
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

class InterviewTrainerFeatureImpl(private val trainerApi: InterviewTrainerApi) : FeatureApi {
    override fun getFeatureName(): String = FeatureRoute.InterviewTrainerFeature.FEATURE_NAME

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier,
    ) {
        val currentPath = pathManager.getCurrentPath()
        Timber.d("InterviewTrainerFeatureImpl registerGraph: currentPath: $currentPath")

        val createQuizRoute = if (currentPath.isEmpty()) {
            getFeatureName()
        } else {
            pathManager.createChildPath(getFeatureName())
        }
        Timber.d("InterviewTrainerFeatureImpl registerGraph: Registering route: $createQuizRoute")

        navGraphBuilder.composable(createQuizRoute) {
            trainerApi.CreateQuizScreen(
                onBackClick = {
                    handleBackNavigation(pathManager, navController)
                },
                onStartTrainingClick = { specializationId, questionsCount ->
                    handleQuizNavigation(
                        pathManager,
                        navController,
                        specializationId,
                        questionsCount
                    )
                }
            )
        }
    }

    /**
     * Обработка навигации назад.
     */
    private fun handleBackNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
    ) {
        val parentPath = pathManager.getCurrentPath()
        Timber.d("InterviewTrainerFeatureImpl handleBackNavigation: Navigating to parent: $parentPath")

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

    /**
     * Обработка навигации к экрану тренировки (InterviewQuizScreen).
     */
    private fun handleQuizNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
        specializationId: String,
        questionsCount: String,
    ) {
        pathManager.setCurrentPath(getFeatureName())

        // Используем текущий путь как базу для экрана тренировки
        val quizPath = pathManager.createParametrizedPath(
            featureName = "quiz",
            "specializationId",
            "questionCount"
        )

        val concretePath = pathManager.createConcretePath(
            parametrizedPath = quizPath,
            specializationId,
            questionsCount
        )
        Timber.d("InterviewTrainerFeatureImpl handleQuizNavigation: Navigating to: $concretePath")

        pathManager.setCurrentPath(concretePath)
        navController.navigate(concretePath)
    }
}