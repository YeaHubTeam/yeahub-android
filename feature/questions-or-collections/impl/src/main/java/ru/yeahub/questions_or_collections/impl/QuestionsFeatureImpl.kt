package ru.yeahub.questions_or_collections.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import ru.yeahub.questions_or_collections.impl.screen.QuestionsScreen
import timber.log.Timber

class QuestionsFeatureImpl : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.QuestionsFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = true

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val currentPath = pathManager.getCurrentPath()

        val questionsRoute = if (currentPath.isEmpty()) {
            getFeatureName()
        } else {
            pathManager.createChildPath(getFeatureName())
        }

        navGraphBuilder.composable(route = questionsRoute) {
            QuestionsScreen(
                onNextClick = {
                    //NO WORK - nav to collections/public_collections/{specId}/{specTitle}
                    pathManager.setCurrentPath(getFeatureName())
                    val questionsPath = pathManager.createChildPath(
                        featureName = FeatureRoute.SpecializationsFeature.FEATURE_NAME
                    )
//                    val questionsPath =
//                        "questions" + "/" +
//                        FeatureRoute.PublicQuestionsFeature.FEATURE_NAME +
//                        "/" + "All"
                    Timber.tag("QuestionFeatureImpl").d("questionsPath = $questionsPath")
                    pathManager.setCurrentPath(questionsPath)
                    navController.navigate(questionsPath)
                }
            )
        }
    }
}