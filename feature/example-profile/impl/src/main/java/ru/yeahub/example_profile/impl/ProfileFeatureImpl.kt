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
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

class ProfileFeatureImpl(private val profileScreen: ProfileScreenApi) : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.ProfileFeature.FEATURE_NAME
    
    override fun isRootFeature(): Boolean = false  // Вложенная фича

    override fun initialize(pathManager: NavigationPathManager) {
        super.initialize(pathManager)
        Timber.d("ProfileFeatureImpl initialize: Registering profile feature paths")
        
        // Дополнительная инициализация для Profile фичи
        // Может включать регистрацию специфичных для фичи путей
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val currentPath = pathManager.getCurrentPath()
        Timber.d("ProfileFeatureImpl registerGraph: currentPath: $currentPath")
        
        // Создаем маршрут профиля с учетом текущего пути
        val profileRoute = pathManager.createParametrizedPath(
            featureName = getFeatureName(),
            "userId",
            "userName"
        )
        
        Timber.d("ProfileFeatureImpl registerGraph: Registering route: $profileRoute")
        
        navGraphBuilder.composable(
            route = profileRoute,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            
            Timber.d("ProfileFeatureImpl composable: userId=$userId, userName=$userName")
            
            // Создаем отображаемый путь для пользователя
            val displayPath = createDisplayPath(pathManager, userId, userName)
            
            profileScreen.ProfileScreen(
                userId = userId,
                userName = userName,
                parentRoute = displayPath,
                onBackClick = {
                    handleBackNavigation(pathManager, navController)
                }
            )
        }
    }
    
    /**
     * Создает отображаемый путь для пользователя.
     */
    private fun createDisplayPath(
        pathManager: NavigationPathManager,
        userId: String,
        userName: String
    ): String {
        val currentPath = pathManager.getCurrentPath()
        return if (currentPath.isEmpty()) {
            "${getFeatureName()}/$userId/$userName"
        } else {
            "$currentPath/${getFeatureName()}/$userId/$userName"
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
        
        Timber.d("ProfileFeatureImpl handleBackNavigation: Navigating to parent: $parentPath")
        
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