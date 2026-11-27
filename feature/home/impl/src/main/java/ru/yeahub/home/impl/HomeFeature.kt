package ru.yeahub.home.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.home.impl.presentation.screen.HomeScreen
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager

internal class HomeFeature : FeatureApi {
    override fun getFeatureName(): String = FeatureRoute.HomeFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = true

    override fun initialize(pathManager: NavigationPathManager) {
        pathManager.registerFeaturePath(getFeatureName(), getFeatureName())
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(getFeatureName()) {
            HomeScreen(modifier = modifier)
        }
    }
}
