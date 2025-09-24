package ru.yeahub.questions_or_collections.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import ru.yeahub.questions_or_collections.impl.screen.CollectionsScreen
import timber.log.Timber

class CollectionsFeatureImpl : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.CollectionsFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = true

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
//        val currentPath = pathManager.getCurrentPath()

//        val collectionsRoute = if (currentPath.isEmpty()) {
//            getFeatureName()
//        } else {
//            pathManager.createChildPath(getFeatureName())
//        }
        val collectionsRoute = getFeatureName()

        navGraphBuilder.composable(route = collectionsRoute) {
            CollectionsScreen(
                onNextClick = {
                    pathManager.setCurrentPath(getFeatureName())
                    val collectionsPath = pathManager.createChildPath(
                        featureName = FeatureRoute.SpecializationsFeature.FEATURE_NAME
                    )
                    Timber.tag("CollectionFeatureImpl").d("collectionsPath = $collectionsPath")
                    //pathManager.setCurrentPath(collectionsPath)
                    navController.navigate(collectionsPath)
                }
            )
        }
    }
}