package ru.yeahub.example_home.impl

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.example_home.api.HomeScreenApi
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute

class HomeFeatureImpl(private val homeScreen: HomeScreenApi) : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.HomeFeature.FEATURE_NAME
    
    override fun isRootFeature(): Boolean = true  // Корневая фича

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        parentRoute: String,
        modifier: Modifier
    ) {
        Log.d("NavDebug", "registerGraph() of HomeFeatureImpl with parentRoute: $parentRoute")
        
        // Корневая фича регистрирует только свой корневой маршрут
        val currentHomeRoute = if (parentRoute.isEmpty()) {
            getFeatureName()
        } else {
            FeatureRoute.createFeatureRoute(parentRoute, getFeatureName())
        }
        
        navGraphBuilder.composable(currentHomeRoute) {
            homeScreen.HomeScreen(
                onProfileClick = { userId, userName ->
                    val profilePath = FeatureRoute.ProfileFeature.createProfilePath(
                        parentRoute = currentHomeRoute,
                        userId = userId,
                        userName = userName
                    )
                    Log.d("NavDebug", "Navigating to profile: $profilePath")
                    navController.navigate(profilePath)
                },
                onQuestionClick = {
                    val questionsPath = FeatureRoute.QuestionsFeature.createQuestionsPath(
                        parentRoute = currentHomeRoute
                    )
                    Log.d("NavDebug", "Navigating to questions: $questionsPath")
                    navController.navigate(questionsPath)
                }
            )
        }
    }
}
