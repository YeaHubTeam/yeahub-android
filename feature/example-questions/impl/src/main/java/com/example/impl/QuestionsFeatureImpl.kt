package com.example.impl

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.api.QuestionsScreenApi
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute

class QuestionsFeatureImpl(private val questionScreen: QuestionsScreenApi) : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.QuestionsFeature.FEATURE_NAME
    
    override fun isRootFeature(): Boolean = false  // Вложенная фича

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        parentRoute: String,
        modifier: Modifier
    ) {
        Log.d("NavDebug", "registerGraph() of QuestionsFeatureImpl with parentRoute: $parentRoute")
        
        // Создаем маршрут для экрана вопросов с учетом родительского маршрута
        val currentQuestionsRoute = FeatureRoute.QuestionsFeature.createQuestionsRoute(parentRoute)
        
        navGraphBuilder.composable(currentQuestionsRoute) {
            questionScreen.QuestionsScreen(
                onBackClick = {
                    Log.d("NavDebug", "Navigating back from questions")
                    navController.navigateUp()
                }
            )
        }
    }
} 