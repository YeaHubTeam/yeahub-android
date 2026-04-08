package ru.yeahub.profile.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.NavigationPathManager

private const val TITLE_TOP_APP_BAR = "title"
private const val NOT_FOUND_NUMBER = 404

class ProfileFeatureImpl : FeatureApi {
    override fun getFeatureName(): String {
        TODO("Not yet implemented")
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        TODO("Not yet implemented")
    }
}