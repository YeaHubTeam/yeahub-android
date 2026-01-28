package ru.yeahub.profile_edit.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager

class ProfileEditFeatureImpl : FeatureApi {
    override fun getFeatureName(): String = FeatureRoute.ProfileEditFeature.FEATURE_NAME


    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier,
    ) {
        TODO("Not yet implemented")
    }
}