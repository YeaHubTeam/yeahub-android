package ru.yeahub.selection_specializations.impl

import SpecializationsScreenApi
import SpecializationsScreenResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

class SpecializationsFeatureImpl(
    val specializationScreen: SpecializationsScreenApi
) : FeatureApi {
    override fun getFeatureName(): String =
        FeatureRoute.SpecializationsFeature.FEATURE_NAME

    override fun isRootFeature(): Boolean = false

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val currentPath = pathManager.getCurrentPath()
        Timber.d("SpecializationsFeatureImpl registerGraph: currentPath: $currentPath")

        // Корневая фича регистрирует только свой корневой маршрут
        val currentHomeRoute = if (currentPath.isEmpty()) {
            getFeatureName()
        } else {
            pathManager.createChildPath(getFeatureName())
        }

        Timber.d("SpecializationsFeatureImpl registerGraph: Registering route: $currentHomeRoute")

        navGraphBuilder.composable(currentHomeRoute) { backStackEntry ->

            Scaffold(
                topBar = {
                    TopAppBarWithBottomBorder(
                        title = TextOrResource.Resource(R.string.selection_specializations_top_bar_title),
                        onBackClick = { SpecializationsScreenResult.NavigateBack }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    //связываем коллбеки из конструктора экрана с логикой навигации
                    specializationScreen.SpecializationScreen(
                        headerText = TextOrResource.Resource(R.string.selection_specializations_list_header),
                        parentRoute = pathManager.getParentPath(),
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
                                        specId = result.specId
                                    )
                                }
                            }
                        },
                    )
                }
            }
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
        specId: String
    ) {
        // Сбрасываем текущий путь на корневую фичу
        pathManager.setCurrentPath(getFeatureName())

        // parentPath (colections(не готово),questions)
        // TODO() - video process (navigate)
        val profilePath = pathManager.createParametrizedPath(
            featureName = FeatureRoute.SpecializationsFeature.FEATURE_NAME,
            specId
        )

        val concretePath = pathManager.createConcretePath(
            profilePath,
            specId
        )

        Timber.d("HomeFeatureImpl handleProfileNavigation: Navigating to: $concretePath")

        pathManager.setCurrentPath(concretePath)
        navController.navigate(concretePath)
    }

    /**
     * Обработка навигации назад.
     */
    private fun handleBackNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController
    ) {
        val parentPath = pathManager.getParentPath()

        Timber.d("SpecializationsFeatureImpl handleBackNavigation: Navigating to parent: $parentPath")

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