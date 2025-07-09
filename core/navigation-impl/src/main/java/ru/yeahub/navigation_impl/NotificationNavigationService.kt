package ru.yeahub.navigation_impl

import android.content.Intent
import androidx.navigation.NavHostController
import ru.yeahub.navigation_api.DeepLinkConfig
import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber

/**
 * Сервис для обработки навигации из уведомлений.
 * 
 * Поддерживает:
 * - Dynamic deep links из уведомлений
 * - Различные типы экранов через DeepLinkConfig
 * - Правильную настройку back stack для корректной навигации назад
 */
class NotificationNavigationService(
    private val pathManager: NavigationPathManager
) {
    
    /**
     * Обрабатывает Intent из уведомления.
     * 
     * @param intent Intent с данными уведомления
     * @param navController NavHostController для навигации
     * @return true, если Intent был обработан
     */
    fun handleNotificationIntent(intent: Intent, navController: NavHostController): Boolean {
        val data = intent.data ?: return false
        val parser = DeepLinkConfig.Utils.parseDeepLink(data)
        
        return if (parser.isValidDeepLink()) {
            handleDeepLink(parser, navController)
            true
        } else {
            Timber.w("Invalid deep link: $data")
            false
        }
    }
    
    /**
     * Обрабатывает deep link для любого типа экрана.
     * 
     * @param parser Парсер deep link
     * @param navController NavHostController для навигации
     */
    private fun handleDeepLink(parser: DeepLinkConfig.DeepLinkParser, navController: NavHostController) {
        val screenType = parser.getScreenType() ?: run {
            Timber.e("Unknown screen type")
            return
        }
        
        when (screenType) {
            DeepLinkConfig.ScreenType.DETAILS -> handleDetailsDeepLink(parser, navController)
            DeepLinkConfig.ScreenType.PROFILE -> handleProfileDeepLink(parser, navController)
            DeepLinkConfig.ScreenType.QUESTIONS -> handleQuestionsDeepLink(parser, navController)
            DeepLinkConfig.ScreenType.HOME -> handleHomeDeepLink(parser, navController)
            DeepLinkConfig.ScreenType.SETTINGS -> handleSettingsDeepLink(parser, navController)
        }
    }
    
    /**
     * Обрабатывает deep link для экрана деталей.
     */
    private fun handleDetailsDeepLink(parser: DeepLinkConfig.DeepLinkParser, navController: NavHostController) {
        val itemId = parser.getItemId() ?: run {
            Timber.e("Missing itemId parameter")
            return
        }
        
        val title = parser.getTitle()
        val rootFeature = parser.getRootFeature()
        
        navigateToDetails(rootFeature, itemId, title, navController)
    }
    
    /**
     * Обрабатывает deep link для экрана профиля.
     */
    private fun handleProfileDeepLink(parser: DeepLinkConfig.DeepLinkParser, navController: NavHostController) {
        val userId = parser.getUserId() ?: run {
            Timber.e("Missing userId parameter")
            return
        }
        
        val userName = parser.getUserName() ?: "Пользователь"
        val rootFeature = parser.getRootFeature()
        
        navigateToProfile(rootFeature, userId, userName, navController)
    }
    
    /**
     * Обрабатывает deep link для экрана вопросов.
     */
    private fun handleQuestionsDeepLink(parser: DeepLinkConfig.DeepLinkParser, navController: NavHostController) {
        val rootFeature = parser.getRootFeature()
        val category = parser.getCategory()
        val filter = parser.getFilter()
        
        navigateToQuestions(rootFeature, category, filter, navController)
    }
    
    /**
     * Обрабатывает deep link для главного экрана.
     */
    private fun handleHomeDeepLink(parser: DeepLinkConfig.DeepLinkParser, navController: NavHostController) {
        val tab = parser.getTab()
        navigateToHome(tab, navController)
    }
    
    /**
     * Обрабатывает deep link для экрана настроек.
     */
    private fun handleSettingsDeepLink(parser: DeepLinkConfig.DeepLinkParser, navController: NavHostController) {
        val category = parser.getCategory()
        navigateToSettings(category, navController)
    }
    
    /**
     * Выполняет навигацию к экрану деталей.
     */
    private fun navigateToDetails(
        rootFeature: String,
        itemId: String,
        title: String,
        navController: NavHostController
    ) {
        val directPath = pathManager.createDirectPath(
            rootFeatureName = rootFeature,
            childFeatureName = DeepLinkConfig.ScreenType.DETAILS.childFeatureName,
            itemId,
            title
        )
        
        navigateWithDirectPath(directPath, navController)
    }
    
    /**
     * Выполняет навигацию к экрану профиля.
     */
    private fun navigateToProfile(
        rootFeature: String,
        userId: String,
        userName: String,
        navController: NavHostController
    ) {
        val directPath = pathManager.createDirectPath(
            rootFeatureName = rootFeature,
            childFeatureName = DeepLinkConfig.ScreenType.PROFILE.childFeatureName,
            userId,
            userName
        )
        
        navigateWithDirectPath(directPath, navController)
    }
    
    /**
     * Выполняет навигацию к экрану вопросов.
     */
    private fun navigateToQuestions(
        rootFeature: String,
        category: String,
        filter: String?,
        navController: NavHostController
    ) {
        val directPath = if (filter != null) {
            pathManager.createDirectPath(
                rootFeatureName = rootFeature,
                childFeatureName = DeepLinkConfig.ScreenType.QUESTIONS.childFeatureName,
                category,
                filter
            )
        } else {
            pathManager.createDirectPath(
                rootFeatureName = rootFeature,
                childFeatureName = DeepLinkConfig.ScreenType.QUESTIONS.childFeatureName,
                category
            )
        }
        
        navigateWithDirectPath(directPath, navController)
    }
    
    /**
     * Выполняет навигацию к главному экрану.
     */
    private fun navigateToHome(tab: String, navController: NavHostController) {
        val directPath = pathManager.createDirectPath(
            rootFeatureName = DeepLinkConfig.ScreenType.HOME.childFeatureName,
            childFeatureName = tab
        )
        
        navigateWithDirectPath(directPath, navController)
    }
    
    /**
     * Выполняет навигацию к экрану настроек.
     */
    private fun navigateToSettings(category: String, navController: NavHostController) {
        val directPath = pathManager.createDirectPath(
            rootFeatureName = DeepLinkConfig.ScreenType.SETTINGS.childFeatureName,
            childFeatureName = category
        )
        
        navigateWithDirectPath(directPath, navController)
    }
    
    /**
     * Общий метод для навигации с direct path.
     */
    private fun navigateWithDirectPath(directPath: String, navController: NavHostController) {
        pathManager.prepareForDirectNavigation(directPath)
        
        navController.navigate(directPath) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    /**
     * Проверяет, может ли сервис обработать данный Intent.
     * 
     * @param intent Intent для проверки
     * @return true, если Intent может быть обработан
     */
    fun canHandleIntent(intent: Intent): Boolean {
        return DeepLinkConfig.Utils.isValidDeepLink(intent)
    }
} 