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
        val featurePath = pathManager.createParametrizedPath(
            getFeatureName(),
            SKILL_FILTER
        )

        navGraphBuilder.composable(
            route = featurePath,
            arguments = listOf(
                navArgument(SKILL_FILTER) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val skillFilter = backStackEntry
                .arguments
                ?.getString(SKILL_FILTER) ?: ""
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
                heading = skillFilter
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

private const val SKILL_FILTER = "skillFilter"