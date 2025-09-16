package ru.yeahub.questions_or_collections.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import ru.yeahub.questions_or_collections.impl.screen.QuestionsScreen

class QuestionsFeatureImpl : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.QuestionsFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = true

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val currentPath = pathManager.getCurrentPath()

        val questionsRoute = if (currentPath.isEmpty()) {
            getFeatureName()
        } else {
            pathManager.createChildPath(getFeatureName())
        }

        navGraphBuilder.composable(route = questionsRoute) {
            QuestionsScreen {
                TODO("Next screen Navigation")
                handleNavigation(pathManager, navController)
            }
        }
    }

    private fun handleNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
    ) {
        TODO()
    }
}