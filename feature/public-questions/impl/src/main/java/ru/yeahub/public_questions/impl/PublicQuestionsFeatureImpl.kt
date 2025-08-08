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
        val featurePath = pathManager.createParametrizedPath(
            getFeatureName(),
            "skillFilter",
            "skills",
            "heading"
        )

        navGraphBuilder.composable(
            route = featurePath,
            arguments = listOf(
                navArgument("skillFilter") { type = NavType.StringType },
                navArgument("skills") { type = NavType.StringType },
                navArgument("heading") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val heading = backStackEntry
                .arguments
                ?.getString("heading") ?: ""
            val skills = backStackEntry.arguments
                ?.getString("skills")
                ?.split(",") ?: listOf()
            val skillFilter = backStackEntry
                .arguments
                ?.getString("skillFilter") ?: ""
            PublicQuestionsScreen(
                onResult = { result ->
                    when (result) {
                        is PublicQuestionsResult.NavigateBack -> handleBackNavigation(
                            pathManager,
                            navController
                        )

                        is PublicQuestionsResult.NavigateToDetail -> {
                            val detailPath = pathManager.createParametrizedPath(
                                FeatureRoute.DetailsFeature.FEATURE_NAME,
                                result.id
                            )
                            navController.navigate(detailPath)
                        }
                    }
                },
                skills = skills,
                skillFilter = skillFilter,
                heading = heading
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