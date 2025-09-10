package ru.yeahub.questions_or_collections.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager

class QuestionsFeatureImpl : FeatureApi {
    override fun getFeatureName(): String = FeatureRoute.QuestionsFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = true

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        TODO("Not yet implemented")
    }
}