package ru.yeahub.detail_question.impl.presentation

import android.content.Intent
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.yeahub.detail_question.impl.presentation.intents.DetailQuestionResult
import ru.yeahub.detail_question.impl.presentation.view.DetailQuestionScreen
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager

class DetailQuestionFeatureImpl : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.DetailQuestionFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = false

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val detailQuestionRoute = pathManager.createParametrizedPath(
            featureName = getFeatureName(),
            "questionId"
        )

        navGraphBuilder.composable(
            route = detailQuestionRoute,
            arguments = listOf(
                navArgument("questionId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val questionId = backStackEntry.arguments?.getLong("questionId") ?: 0L
            DetailQuestionScreen(
                onResult = { result ->
                    when (result) {
                        DetailQuestionResult.BackClick -> handleBackNavigation(
                            pathManager,
                            navController
                        )
                        is DetailQuestionResult.UrlClick -> {
                            val intent = Intent(Intent.ACTION_VIEW, result.url.toUri())
                            navController.context.startActivity(intent)
                        }
                    }
                },
                questionId = questionId
            )
        }
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