package ru.yeahub.example_home.impl

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.yeahub.navigation_api.NotificationService

/**
 * Пример использования NotificationService в фиче Home.
 */
class HomeNotificationExample : KoinComponent {

    private val notificationService: NotificationService by inject()

    fun showNewsNotification(newsId: String, title: String, summary: String): Boolean =
        notificationService.showDetailsNotification(
            itemId = newsId,
            title = title,
            message = summary,
            rootFeature = "home"
        )

    fun showImportantEventNotification(eventId: String, eventTitle: String) {
        notificationService.createNotificationChannel(
            channelId = "home_important_events",
            channelName = "Важные события",
            channelDescription = "Уведомления о важных событиях на главной странице",
            importance = NotificationService.NotificationImportance.HIGH
        )

        notificationService.showDetailsNotification(
            itemId = eventId,
            title = "🔥 $eventTitle",
            message = "Не пропустите важное событие!",
            rootFeature = "home"
        )
    }

    fun createShareableLink(itemId: String, title: String): String =
        notificationService.createDetailsDeepLink(
            itemId = itemId,
            title = title,
            rootFeature = "home"
        )

    fun canShowNotifications(): Boolean = notificationService.areNotificationsEnabled()

    fun showExampleNotifications() {
        if (!canShowNotifications()) return

        showNewsNotification(
            "news_001",
            "Новости технологий",
            "Обзор последних новостей в мире технологий"
        )
        showImportantEventNotification("event_002", "Специальное предложение")
    }
} 