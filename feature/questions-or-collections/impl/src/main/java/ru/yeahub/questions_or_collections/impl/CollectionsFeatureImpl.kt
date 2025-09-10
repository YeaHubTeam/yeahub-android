package ru.yeahub.questions_or_collections.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import ru.yeahub.questions_or_collections.impl.screen.CollectionsScreen

class CollectionsFeatureImpl : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.CollectionsFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = true

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val collectionsRoute = pathManager.createParametrizedPath(getFeatureName())

        navGraphBuilder.composable(route = collectionsRoute) { backStackEntry ->
            CollectionsScreen {
                handleBackNavigation(
                    pathManager,
                    navController
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
}