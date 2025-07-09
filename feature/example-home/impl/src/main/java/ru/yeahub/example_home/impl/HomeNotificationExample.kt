package ru.yeahub.example_home.impl

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.yeahub.navigation_api.NotificationService
import timber.log.Timber

/**
 * Пример использования NotificationService в фиче Home.
 * 
 * Демонстрирует, как любая фича может использовать универсальный сервис уведомлений
 * через navigation-api без прямой зависимости от реализации.
 */
class HomeNotificationExample : KoinComponent {
    
    private val notificationService: NotificationService by inject()
    
    /**
     * Показывает уведомление о новости с переходом к деталям.
     */
    fun showNewsNotification(newsId: String, title: String, summary: String) {
        Timber.d("HomeNotificationExample showNewsNotification: $newsId")
        
        val success = notificationService.showDetailsNotification(
            itemId = newsId,
            title = title,
            message = summary,
            rootFeature = "home"
        )
        
        if (success) {
            Timber.d("HomeNotificationExample showNewsNotification: Notification shown successfully")
        } else {
            Timber.w("HomeNotificationExample showNewsNotification: Failed to show notification")
        }
    }
    
    /**
     * Показывает уведомление о важном событии.
     */
    fun showImportantEventNotification(eventId: String, eventTitle: String) {
        val customChannelId = "home_important_events"
        
        // Создаем специальный канал для важных событий
        notificationService.createNotificationChannel(
            channelId = customChannelId,
            channelName = "Важные события",
            channelDescription = "Уведомления о важных событиях на главной странице",
            importance = NotificationService.NotificationImportance.HIGH
        )
        
        // Показываем уведомление
        notificationService.showDetailsNotification(
            itemId = eventId,
            title = "🔥 $eventTitle",
            message = "Не пропустите важное событие!",
            rootFeature = "home"
        )
    }
    
    /**
     * Создает Deep Link для обмена.
     */
    fun createShareableLink(itemId: String, title: String): String {
        return notificationService.createDetailsDeepLink(
            itemId = itemId,
            title = title,
            rootFeature = "home"
        )
    }
    
    /**
     * Отменяет все уведомления от фичи Home.
     */
    fun cancelAllHomeNotifications() {
        // В реальном приложении здесь можно было бы отслеживать ID уведомлений фичи
        // и отменять только их, но для примера отменим все
        Timber.d("HomeNotificationExample cancelAllHomeNotifications")
    }
    
    /**
     * Проверяет, можно ли показывать уведомления.
     */
    fun canShowNotifications(): Boolean {
        return notificationService.areNotificationsEnabled()
    }
    
    /**
     * Показывает различные примеры уведомлений для демонстрации.
     */
    fun showExampleNotifications() {
        // Проверяем, разрешены ли уведомления
        if (!canShowNotifications()) {
            Timber.w("HomeNotificationExample showExampleNotifications: Notifications are disabled")
            return
        }
        
        // Пример 1: Обычная новость
        showNewsNotification(
            newsId = "news_001",
            title = "Новости технологий",
            summary = "Обзор последних новостей в мире технологий"
        )
        
        // Пример 2: Важное событие
        showImportantEventNotification(
            eventId = "event_002",
            eventTitle = "Специальное предложение"
        )
        
        // Пример 3: Создание ссылки для обмена
        val shareLink = createShareableLink(
            itemId = "share_003",
            title = "Интересная статья"
        )
        Timber.d("HomeNotificationExample showExampleNotifications: Share link created: $shareLink")
    }
} 