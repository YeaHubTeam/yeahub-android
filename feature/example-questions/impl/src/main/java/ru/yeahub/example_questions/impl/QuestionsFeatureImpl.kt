package ru.yeahub.example_questions.impl

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.yeahub.example_questions.api.QuestionsScreenApi
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

class QuestionsFeatureImpl(private val questionScreen: QuestionsScreenApi) : FeatureApi {

    override fun getFeatureName(): String = FeatureRoute.QuestionsFeature.FEATURE_NAME
    
    override fun isRootFeature(): Boolean = false  // Вложенная фича

    override fun initialize(pathManager: NavigationPathManager) {
        super.initialize(pathManager)
        Timber.d("QuestionsFeatureImpl initialize: Registering questions feature paths")
        
        // Дополнительная инициализация для Questions фичи
        // Может включать регистрацию специфичных для фичи путей
    }

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        pathManager: NavigationPathManager,
        modifier: Modifier
    ) {
        val currentPath = pathManager.getCurrentPath()
        Timber.d("QuestionsFeatureImpl registerGraph: currentPath: $currentPath")
        
        // Создаем маршрут для экрана вопросов с учетом текущего пути
        val questionsRoute = pathManager.createChildPath(getFeatureName())
        
        Timber.d("QuestionsFeatureImpl registerGraph: Registering route: $questionsRoute")
        
        navGraphBuilder.composable(questionsRoute) {
            questionScreen.QuestionsScreen(
                onBackClick = {
                    handleBackNavigation(pathManager, navController)
                },
                onDetailsClick = { itemId, title ->
                    handleDetailsNavigation(pathManager, navController, itemId, title)
                }
            )
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
        
        Timber.d("QuestionsFeatureImpl handleBackNavigation: Navigating to parent: $parentPath")
        
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
    
    /**
     * Обработка навигации к деталям.
     */
    private fun handleDetailsNavigation(
        pathManager: NavigationPathManager,
        navController: NavHostController,
        itemId: String,
        title: String
    ) {
        // Используем текущий путь как базу для Details
        val detailsPath = pathManager.createParametrizedPath(
            featureName = "details",
            "itemId",
            "title"
        )
        
        val concretePath = pathManager.createConcretePath(
            detailsPath,
            itemId,
            title
        )
        
        Timber.d("QuestionsFeatureImpl handleDetailsNavigation: Navigating to: $concretePath")
        
        pathManager.setCurrentPath(concretePath)
        navController.navigate(concretePath)
    }
} 