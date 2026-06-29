package ru.yeahub.interview_trainer.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizResult
import ru.yeahub.interview_trainer.impl.createQuiz.ui.CreateQuizScreen
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizResult
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.VoQuestionWithAnswer
import ru.yeahub.interview_trainer.impl.interviewQuiz.ui.InterviewQuizScreen
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

private const val SPECIALIZATION_ID = "specializationId"
private const val QUESTIONS_COUNT = "questionsCount"
private const val QUIZ_ANSWERS_KEY = "quizAnswersKey"

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
            "$featurePath/${FeatureRoute.InterviewTrainerFeature.CREATE_QUIZ_SCREEN_NAME}"

        // Регистрируем путь экрана тренировки
        // (interview_trainer/interview_quiz/{specializationId}/{questionsCount})
        val interviewQuizRoute =
            "$featurePath/${FeatureRoute.InterviewTrainerFeature.INTERVIEW_QUIZ_SCREEN_NAME}" +
                    "/{$SPECIALIZATION_ID}/{$QUESTIONS_COUNT}"

        Timber.d("InterviewTrainerFeatureImpl registerGraph: currentPath: $createQuizRoute")

        navGraphBuilder.composable(
            route = createQuizRoute,
        ) { _ ->
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
                        specializationId = result.specializationId.toString(),
                        questionsCount = result.questionCount.toString()
                    )
                }
            }
            CreateQuizScreen(onResult = onResult)
        }

        navGraphBuilder.composable(
            route = interviewQuizRoute,
            arguments = listOf(
                navArgument(SPECIALIZATION_ID) { type = NavType.StringType },
                navArgument(QUESTIONS_COUNT) { type = NavType.StringType },
            )
        ) { _ ->
            InterviewQuizScreen(
                onResult = { result ->
                    when (result) {
                        is InterviewQuizResult.NavigateBack -> handleBackNavigation(
                            pathManager = pathManager,
                            navController = navController
                        )

                        is InterviewQuizResult.NavigateToInterviewQuizResultScreen -> handleQuizResultNavigation(
                            pathManager = pathManager,
                            navController = navController,
                            featurePath = featurePath,
                            questionsWithAnswersList = result.questionsWithAnswersList
                        )
                    }
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
        specializationId: String,
        questionsCount: String,
    ) {
        //Регистрируем путь экрана тренировки (interview_trainer/create_quiz/{titleId})
        val interviewQuizRoute = featurePath + "/" +
                FeatureRoute.InterviewTrainerFeature.INTERVIEW_QUIZ_SCREEN_NAME + "/" +
                "$specializationId/$questionsCount"

        Timber.d("InterviewTrainerFeatureImpl registerGraph: $interviewQuizRoute")

        navController.navigate(interviewQuizRoute)
        pathManager.setCurrentPath(interviewQuizRoute)
    }

    /**
     * Обработка навигации к экрану результата тренировки (InterviewQuizResultScreen)
     */
    private fun handleQuizResultNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
        featurePath: String,
        questionsWithAnswersList: List<VoQuestionWithAnswer>
    ) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.set(QUIZ_ANSWERS_KEY, ArrayList(questionsWithAnswersList))

        val interviewQuizResultRoute =
            "$featurePath/${FeatureRoute.InterviewTrainerFeature.INTERVIEW_QUIZ_RESULT_SCREEN_NAME}"

        Timber.d("InterviewTrainerFeatureImpl registerGraph: $interviewQuizResultRoute")

        navController.navigate(interviewQuizResultRoute)
        pathManager.setCurrentPath(interviewQuizResultRoute)
    }
}