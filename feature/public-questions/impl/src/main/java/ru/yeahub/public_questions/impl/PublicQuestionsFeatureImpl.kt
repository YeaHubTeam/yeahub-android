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

private const val TITTLE_TOP_APP_BAR = "tittle"
private const val ID_COLLECTION = "idCollection"
private const val ID_SPECIALIZATION = "idSpecialization"

/**
 * Передача параметра в feature publisc-questions:
 * - публичная страница вопросов
 * @public_questions/"tittleTopAppBar + ?skillFilter="Категория вопроса"
 * - коллекции
 * @public_questions/tittleTopAppBar + &idCollection="Категория вопроса"
 */
class PublicQuestionsFeatureImpl : FeatureApi {
    override fun getFeatureName(): String = FeatureRoute.PublicQuestionsFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean {
        return false
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val currentTabPrefix = pathManager.getCurrentPath()
        val featureName = getFeatureName()

        val routePattern = "$currentTabPrefix/$featureName?" +
                "$TITTLE_TOP_APP_BAR={$TITTLE_TOP_APP_BAR}" +
                "&$ID_SPECIALIZATION={$ID_SPECIALIZATION}" +
                "&$ID_COLLECTION={$ID_COLLECTION}"
        navGraphBuilder.composable(
            route = routePattern,
            arguments = listOf(
                navArgument(ID_SPECIALIZATION) {
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
            val tittleTopAppBar = backStackEntry
                .arguments
                ?.getString(TITTLE_TOP_APP_BAR) ?: "All"
            val idCollection =
                backStackEntry.arguments?.getString(ID_COLLECTION)?.toInt()
            val idSpecialization =
                backStackEntry.arguments?.getString(ID_SPECIALIZATION)?.toInt()
            PublicQuestionsScreen(
                onResult = { result ->
                    when (result) {
                        is PublicQuestionsResult.NavigateBack -> handleBackNavigation(
                            pathManager,
                            navController
                        )

                        is PublicQuestionsResult.NavigateToDetail -> {
                            val questionIdsParam = result.questionIds.joinToString(",")
                            val detailRoute =
                                "$currentTabPrefix/" + FeatureRoute.DetailQuestionFeature
                                    .FEATURE_NAME + "/" + questionIdsParam + "/" + result.currentIndex
                            Timber.tag("Test")
                                .d("PublicQuestionsFeatureImpl registerGraph: $detailRoute")
                            navController.navigate(detailRoute)
                        }
                    }
                },
                tittleTopAppBar = tittleTopAppBar,
                idCollection = idCollection,
                idSpecialization = idSpecialization,
            )
            Timber.tag("Test12")
                .d(" Title - $tittleTopAppBar idCollection - $idCollection idSpec - $idSpecialization")
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