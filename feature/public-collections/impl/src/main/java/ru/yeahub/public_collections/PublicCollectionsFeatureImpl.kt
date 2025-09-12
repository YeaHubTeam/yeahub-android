package ru.yeahub.public_collections

import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import ru.yeahub.public_collections.impl.presentation.intents.PublicCollectionsScreenResult
import ru.yeahub.public_collections.impl.ui.PublicCollectionsScreen
import timber.log.Timber

class PublicCollectionsFeatureImpl : FeatureApi {
    override fun getFeatureName(): String = FeatureRoute.PublicCollectionsFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = false

    override fun registerGraph(
        navGraphBuilder: androidx.navigation.NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val featurePath = pathManager.createParametrizedPath(
            getFeatureName(),
            SPECIALIZATION_ID,
            HEADER
        )
        navGraphBuilder.composable(
            route = featurePath,
            arguments = listOf(
                navArgument(SPECIALIZATION_ID) { type = NavType.LongType },
                navArgument(HEADER) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val specializationId = backStackEntry.arguments
                ?.getLong(SPECIALIZATION_ID) ?: 0
            val header = backStackEntry.arguments
                ?.getString(HEADER) ?: ""
            PublicCollectionsScreen(
                onResult = { result ->
                    when (result) {
                        is PublicCollectionsScreenResult.NavigateBack -> handleBackNavigation(
                            pathManager,
                            navController
                        )

                        is PublicCollectionsScreenResult.NavigateToQuestions -> handleQuestionsNavigation(
                            pathManager,
                            navController,
                            result.collectionId.toString()
                        )
                    }
                },
                specializationId = specializationId,
                header = header
            )
        }
    }

    private fun handleQuestionsNavigation( pathManager: NavigationPathManager,
                                           navController: NavHostController,
                                           collectionId: String) {
        pathManager.setCurrentPath(getFeatureName())

        val questionsPath = pathManager.createParametrizedPath(
            featureName = "public_questions",
            "collectionId"
        )

        val concretePath = pathManager.createConcretePath(
            questionsPath,
            collectionId
        )

        Timber.d("PublicCollectionsFeatureImpl handleDetailsNavigation: Navigating to: $concretePath")

        pathManager.setCurrentPath(concretePath)
        navController.navigate(concretePath)
    }

    private fun handleBackNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController
    ) {
        val parentPath = pathManager.getParentPath()

        pathManager.setCurrentPath(parentPath)

        if (parentPath.isEmpty()) {
            navController.navigateUp()
        } else {
            navController.navigate(parentPath) {
                popUpTo(parentPath) {
                    inclusive = true
                }
            }
        }
    }
}


const val SPECIALIZATION_ID = "specializationId"
const val HEADER = "header"