package ru.yeahub.home.impl

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.home.impl.presentation.screen.HomeScreen
import ru.yeahub.home.impl.presentation.state.HomeScreenState
import ru.yeahub.home.impl.presentation.viewmodel.HomeViewModel
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager

class HomeFeature : FeatureApi {
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
            val viewModel = viewModel<HomeViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            when (val currentState = state) {
                is HomeScreenState.Content -> HomeScreen(state = currentState, modifier = modifier)
            }
        }
    }
}
