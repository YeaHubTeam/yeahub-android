package ru.yeahub.selection_specializations.impl

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import ru.yeahub.selection_specializations.impl.ui.SpecializationScreen
import ru.yeahub.selection_specializations.impl.ui.SpecializationsScreenResult
import timber.log.Timber

class QuestionSpecializationsFeatureImpl() : FeatureApi {

    override fun getFeatureName(): String =
        FeatureRoute.SpecializationsFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = false

    @Composable
    fun DefaultSpecializationScreen(
        navController: NavHostController,
        pathManager: NavigationPathManager,
    ) {
        SpecializationScreen(
            headerText = TextOrResource.Resource(R.string.selection_specializations_top_bar_header),
            onResult = { result ->
                when (result) {
                    SpecializationsScreenResult.NavigateBack -> {
                        handleBackNavigation(
                            pathManager = pathManager,
                            navController = navController
                        )
                    }

                    is SpecializationsScreenResult.SpecializationClick -> {
                        handleSpecializationsNavigation(
                            pathManager = pathManager,
                            navController = navController,
                            specId = result.specId.toString(),
                            specTitle = result.specTitle
                        )
                        Timber.tag("Test123")
                            .d(" Title - ${result.specTitle} idSpec - ${result.specId}")
                    }
                }
            }
        )
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val specializationPath = pathManager.createDirectPath(
            rootFeatureName = FeatureRoute.QuestionsFeature.FEATURE_NAME,
            childFeatureName = getFeatureName()
        )

        Timber.tag("Q_SpecFeatureImpl").d("register specializationPath = $specializationPath")

        navGraphBuilder.composable(specializationPath) { backStackEntry ->
            DefaultSpecializationScreen(
                navController = navController,
                pathManager = pathManager
            )
        }
    }

    /**
     * Обработка навигации к специализации.
     * Переход осуществляется изходя из
     * родительской фичи в pathManager и specId.
     */
    fun handleSpecializationsNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
        specId: String,
        specTitle: String
    ) {
        val encodedTitle = Uri.encode(specTitle)
        val nextRoute = "questions/public_questions?tittle=$encodedTitle" +
                "&idSpecialization=$specId"

        Timber.tag("SpecFeatureImpl").d(
            "QuestionSpecializationsFeatureImpl nextRoute: $nextRoute"
        )

        pathManager.setCurrentPath(nextRoute)
        navController.navigate(nextRoute)
    }

    /**
     * Обработка навигации назад.
     */
    private fun handleBackNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController
    ) {
        val parentPath = pathManager.getParentPath()

        Timber
            .tag("SpecFeatureImpl")
            .d("SpecializationsFeatureImpl handleBackNavigation: Navigating to parent: $parentPath")

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