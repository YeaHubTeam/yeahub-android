package ru.yeahub.example_profile.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.yeahub.example_profile.api.ProfileScreenApi
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import timber.log.Timber

class ProfileFeatureImpl(private val profileScreen: ProfileScreenApi) : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.ProfileFeature.FEATURE_NAME
    
    override fun isRootFeature(): Boolean = false  // Вложенная фича

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        parentRoute: String,
        modifier: Modifier
    ) {
        Timber.d("ProfileFeatureImpl registerGraph: parentRoute: $parentRoute")
        
        // Создаем маршрут профиля с учетом родительского маршрута
        val currentProfileRoute = FeatureRoute.ProfileFeature.createProfileRoute(parentRoute)
        
        navGraphBuilder.composable(
            route = currentProfileRoute,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            
            Timber.d("ProfileFeatureImpl composable: userId=$userId, userName=$userName")
            
            profileScreen.ProfileScreen(
                userId = userId,
                userName = userName,
                parentRoute = parentRoute,
                onBackClick = {
                    Timber.d("ProfileFeatureImpl onBackClick: Navigating back from profile")
                    navController.navigateUp()
                }
            )
        }
    }
}