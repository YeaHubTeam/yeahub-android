package ru.yeahub.interview_trainer.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizResult
import ru.yeahub.interview_trainer.impl.createQuiz.ui.CreateQuizScreen
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

private const val TITLE_TOP_APP_BAR = "title"

class InterviewTrainerFeatureImpl() : FeatureApi {
    override fun getFeatureName(): String = FeatureRoute.InterviewTrainerFeature.FEATURE_NAME

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier,
    ) {
        val basePathWithParams = pathManager.createParametrizedPath(
            featureName = getFeatureName() + "/" +
                    FeatureRoute.InterviewTrainerFeature.CREATE_QUIZ_SCREEN_NAME,
            TITLE_TOP_APP_BAR
        )
        Timber.d("InterviewTrainerFeatureImpl registerGraph: currentPath: $basePathWithParams")

        navGraphBuilder.composable(
            route = basePathWithParams,
            arguments = listOf(
                navArgument(TITLE_TOP_APP_BAR) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val titleTopAppBar = backStackEntry.arguments?.getString(TITLE_TOP_APP_BAR) ?: ""

            CreateQuizScreen(
                onResult = { result ->
                    when (result) {
                        is CreateQuizResult.NavigateBack -> handleBackNavigation(
                            pathManager,
                            navController
                        )

                        is CreateQuizResult.NavigateToInterviewQuizScreen -> handleQuizNavigation(
                            pathManager = pathManager,
                            navController = navController,
                            titleTopAppBar = titleTopAppBar,
                            specializationId = result.specializationId.toString(),
                            questionsCount = result.questionCount.toString()
                        )
                    }
                },
                titleTopAppBar = titleTopAppBar
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
        titleTopAppBar: String,
        specializationId: String,
        questionsCount: String,
    ) {
        val interviewQuizRoute = getFeatureName() + "/" +
                FeatureRoute.InterviewTrainerFeature.INTERVIEW_QUIZ_SCREEN_NAME + "/" +
                titleTopAppBar + specializationId + questionsCount

        Timber.d("InterviewTrainerFeatureImpl registerGraph: $interviewQuizRoute")

        navController.navigate(interviewQuizRoute)
    }
}