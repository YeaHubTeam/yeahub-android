package ru.yeahub.example_profile.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.yeahub.example_profile.api.ProfileFeatureApi
import ru.yeahub.example_profile.api.ProfileScreenApi
import ru.yeahub.navigation_api.FeatureRoute

class ProfileFeatureImpl(private val profileScreen: ProfileScreenApi) : ProfileFeatureApi {
    //TODO( Исправить с Хардкода home/ на приходящий маршрут )
    override val profileRoute: String = FeatureRoute.ProfileFeature("home/").profile

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        modifier: Modifier
    ) {
        navGraphBuilder.composable(
            route = profileRoute,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            
            profileScreen.ProfileScreen(
                userId = userId,
                userName = userName,
                parentRoute = "home",
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}