package ru.yeahub.selection_specializations.impl

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

class SpecializationsFeatureImpl() : FeatureApi {
    override fun getFeatureName(): String =
        FeatureRoute.SpecializationsFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = false

    @Composable
    fun DefaultSpecializationScreen(
        navController: NavHostController,
        pathManager: NavigationPathManager,
    ) {
        SpecializationScreen(
            headerText = TextOrResource.Resource(R.string.selection_specializations_list_header),
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
                            specId = result.specId,
                            specTitle = result.specTitle
                        )
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
        val specializationPath = pathManager.createChildPath(
            featureName = getFeatureName()
        )

        Timber.tag("SpecFeatureImpl").d("register specializationPath = $specializationPath")

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
        specId: Long,
        specTitle: String
    ) {
        val nextRoute =
            buildNextRoute(
                pathManager = pathManager,
                specId = specId,
                specTitle = specTitle
            )

        Timber.tag("SpecFeatureImpl").d(
            "SpecializationsFeatureImpl nextRoute: $nextRoute"
        )

        pathManager.setCurrentPath(nextRoute)
        navController.navigate(nextRoute)
    }

    private fun buildNextRoute(
        pathManager: NavigationPathManager,
        specId: Long,
        specTitle: String
    ): String {
        val currentPath = pathManager.getCurrentPath()

        return when {
            currentPath.contains("questions") -> {
                //universal dummy (from questions module)
                "questions" + "/" +
                FeatureRoute.PublicQuestionsFeature.FEATURE_NAME +
                "/" + "All"
            }

            currentPath.contains("collections") -> {
                val createdPath = pathManager.createParametrizedPath(
                    featureName = FeatureRoute.PublicCollectionsFeature.FEATURE_NAME,
                    "specId",
                    "title"
                )

                val concretePath = pathManager.createConcretePath(
                    createdPath,
                    specId.toString(),
                    specTitle
                )

                concretePath
            }

            else -> "home/"
        }
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