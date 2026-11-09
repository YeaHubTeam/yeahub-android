package ru.yeahub.home.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.home.impl.presentation.HomeScreen
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager

/**
 * Главная страница: маршрут "home", корневая фича (таб «Главная»).
 * Состоит из независимых блоков, пока реализован блок статистики собеседований.
 */
class HomeFeatureImpl : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.HomeFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = true

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val homeRoute = pathManager.getCurrentPath().ifEmpty { getFeatureName() }

        navGraphBuilder.composable(route = homeRoute) {
            HomeScreen(
//                onResult = { result -> handleResult(result) }
            )
        }
    }

//    private fun handleResult(result: HomeResult) {
//        when (result) {
//            is OpenStatistics -> {
//                // TODO: перейти к экрану статистики, когда фича появится
//                Timber.d("HomeFeatureImpl: OpenStatistics requested")
//            }
//        }
//    }
}
