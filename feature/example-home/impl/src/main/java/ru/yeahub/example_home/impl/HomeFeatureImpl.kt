package ru.yeahub.example_home.impl

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.example_home.api.HomeFeatureApi
import ru.yeahub.example_home.api.HomeScreenApi
import ru.yeahub.navigation_api.FeatureRoute

class HomeFeatureImpl(private val homeScreen: HomeScreenApi) : HomeFeatureApi {
    override val homeRoute: String = FeatureRoute.HomeFeature.HOME

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        Log.d("NavDebug", "registerGraph() of HomeFeatureImpl")
        navGraphBuilder.composable(homeRoute) {
            homeScreen.HomeScreen(
                onProfileClick = { userId, userName ->
                    navController.navigate(
                        FeatureRoute.ProfileFeature(homeRoute).profileRoute(
                            userId = userId,
                            userName = userName,
                        )
                    )
                }
            )
        }
    }
}
