package ru.yeahub.public_questions.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import ru.yeahub.public_questions.impl.presentation.intents.PublicQuestionsResult
import ru.yeahub.public_questions.impl.presentation.screen.PublicQuestionsScreen
import timber.log.Timber
import kotlin.text.append

private const val TITTLE_TOP_APP_BAR = "tittle"
private const val SKILL_FILTER = "skillFilter"
private const val ID_COLLECTION = "idCollection"

/**
 * Передача параметра в feature publisc-questions:
 * - публичная страница вопросов
 * @public_questions/"tittleTopAppBar + ?skillFilter="Категория вопроса"
 * - коллекции
 * @public_questions/tittleTopAppBar + ?idCollection="Категория вопроса"
 */
class PublicQuestionsFeatureImpl : FeatureApi {
    override fun getFeatureName(): String = FeatureRoute.PublicQuestionsFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean {
        return true
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val routePattern = buildString {
            append(getFeatureName())
            append("/{$TITTLE_TOP_APP_BAR}")
            append("?$SKILL_FILTER={$SKILL_FILTER}")
            append("?$ID_COLLECTION={$ID_COLLECTION}")
        }

        navGraphBuilder.composable(
            route = routePattern,
            arguments = listOf(
                navArgument(SKILL_FILTER) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(TITTLE_TOP_APP_BAR) {
                    type = NavType.StringType
                },
                navArgument(ID_COLLECTION) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val skillFilter = backStackEntry
                .arguments
                ?.getString(SKILL_FILTER) ?: "All"
            val tittleTopAppBar = backStackEntry
                .arguments
                ?.getString(TITTLE_TOP_APP_BAR) ?: "All"
            val idCollection = backStackEntry
                .arguments
                ?.getString(ID_COLLECTION)?.toInt()

            PublicQuestionsScreen(
                onResult = { result ->
                    when (result) {
                        is PublicQuestionsResult.NavigateBack -> handleBackNavigation(
                            pathManager,
                            navController
                        )

                        is PublicQuestionsResult.NavigateToDetail -> {
                            val detailRout =
                                getFeatureName() + "/" + FeatureRoute.DetailQuestionFeature
                                    .FEATURE_NAME + "/" + result.id
                            Timber.tag("Test")
                                .d("PublicQuestionsFeatureImpl registerGraph: $detailRout")
                            navController.navigate(detailRout)
                        }
                    }
                },
                skillFilter = skillFilter,
                tittleTopAppBar = tittleTopAppBar,
                idCollection = idCollection,
                skills = listOf()
            )
        }
    }

    private fun handleBackNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController
    ) {
        val parentPath = pathManager.getParentPath()

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