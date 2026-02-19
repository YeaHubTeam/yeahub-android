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
private const val NOT_FOUND_NUMBER = 404

class InterviewTrainerFeatureImpl : FeatureApi {
    override fun getFeatureName(): String = FeatureRoute.InterviewTrainerFeature.FEATURE_NAME

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier,
    ) {
        //Регистрируем базовый путь фичи (interview_trainer)
        pathManager.registerFeaturePath(featureName = getFeatureName(), basePath = getFeatureName())

        val featurePath = pathManager.getFeaturePath(getFeatureName()) ?: getFeatureName()

        //Регистрируем путь экрана создания тренировки (interview_trainer/create_quiz/{titleId})
        val createQuizRoute =
            "$featurePath/${FeatureRoute.InterviewTrainerFeature.CREATE_QUIZ_SCREEN_NAME}/{$TITLE_TOP_APP_BAR}"

        Timber.d("InterviewTrainerFeatureImpl registerGraph: currentPath: $createQuizRoute")

        navGraphBuilder.composable(
            route = createQuizRoute,
            arguments = listOf(
                navArgument(TITLE_TOP_APP_BAR) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val titleTopAppBarResId =
                backStackEntry.arguments?.getInt(TITLE_TOP_APP_BAR) ?: NOT_FOUND_NUMBER

            // Вынос в отдельную переменную для оптимизации (чтоб не пересоздавать лямбду)
            val onResult = { result: CreateQuizResult ->
                when (result) {
                    is CreateQuizResult.NavigateBack -> handleBackNavigation(
                        pathManager = pathManager,
                        navController = navController
                    )

                    is CreateQuizResult.NavigateToInterviewQuizScreen -> handleQuizNavigation(
                        pathManager = pathManager,
                        navController = navController,
                        featurePath = featurePath,
                        titleTopAppBarResId = titleTopAppBarResId,
                        specializationId = result.specializationId.toString(),
                        questionsCount = result.questionCount.toString()
                    )
                }
            }
            CreateQuizScreen(onResult = onResult, titleTopAppBarResId = titleTopAppBarResId)
        }
    }

    /**
     * Обработка навигации назад.
     */
    private fun handleBackNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
    ) {
        val parentPath = pathManager.getParentPath()
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
        featurePath: String,
        titleTopAppBarResId: Int,
        specializationId: String,
        questionsCount: String,
    ) {
        //Регистрируем путь экрана тренировки (interview_trainer/create_quiz/{titleId})
        val interviewQuizRoute = featurePath + "/" +
                FeatureRoute.InterviewTrainerFeature.INTERVIEW_QUIZ_SCREEN_NAME + "/" +
                "$titleTopAppBarResId/$specializationId/$questionsCount"

        Timber.d("InterviewTrainerFeatureImpl registerGraph: $interviewQuizRoute")

        navController.navigate(interviewQuizRoute)
        pathManager.setCurrentPath(interviewQuizRoute)
    }
}