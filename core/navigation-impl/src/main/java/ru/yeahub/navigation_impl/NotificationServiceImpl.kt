package ru.yeahub.navigation_impl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.yeahub.navigation_api.DeepLinkConfig
import ru.yeahub.navigation_api.NotificationService
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

/**
 * Реализация сервиса уведомлений с dynamic deep links.
 *
 * Поддерживает:
 * - Создание уведомлений с dynamic deep links
 * - Правильную настройку PendingIntent
 * - Управление каналами уведомлений
 * - Различные типы экранов через DeepLinkConfig
 */
class NotificationServiceImpl(
    private val context: Context
) : NotificationService {

    private val notificationIdCounter = AtomicInteger(INITIAL_NOTIFICATION_ID)

    companion object {
        private const val DEFAULT_ICON = android.R.drawable.ic_dialog_info
        private const val INITIAL_NOTIFICATION_ID = 1000
    }

    init {
        // Создаем основные каналы уведомлений
        createDefaultChannels()
    }

    override fun showDetailsNotification(
        itemId: String,
        title: String,
        message: String,
        rootFeature: String,
        notificationId: Int
    ): Boolean {
        val deepLinkBuilder = DeepLinkConfig.Factory.details()
            .itemId(itemId)
            .title(title)
            .rootFeature(rootFeature)

        return showNotification(
            screenType = DeepLinkConfig.ScreenType.DETAILS,
            title = title,
            message = message,
            deepLinkBuilder = deepLinkBuilder,
            notificationId = notificationId
        )
    }

    override fun showNotification(
        screenType: DeepLinkConfig.ScreenType,
        title: String,
        message: String,
        deepLinkBuilder: DeepLinkConfig.DeepLinkBuilder,
        notificationId: Int
    ): Boolean {
        val intent = createNotificationIntent(deepLinkBuilder)
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getChannelIdForScreenType(screenType)
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(DEFAULT_ICON)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        return try {
            if (areNotificationsEnabled()) {
                @Suppress("MissingPermission")
                NotificationManagerCompat.from(context).notify(notificationId, notification)
                true
            } else {
                Timber.w("Notifications are disabled")
                false
            }
        } catch (e: SecurityException) {
            Timber.e(e, "Permission denied for notification")
            false
        }
    }

    override fun createNotificationIntent(
        itemId: String,
        title: String,
        rootFeature: String
    ): Intent {
        return createNotificationIntent(
            DeepLinkConfig.Factory.details()
                .itemId(itemId)
                .title(title)
                .rootFeature(rootFeature)
        )
    }

    override fun createNotificationIntent(deepLinkBuilder: DeepLinkConfig.DeepLinkBuilder): Intent =
        deepLinkBuilder.buildIntent()

    override fun createDetailsDeepLink(
        itemId: String,
        title: String,
        rootFeature: String
    ): String = createDeepLink(
        DeepLinkConfig.Factory.details()
            .itemId(itemId)
            .title(title)
            .rootFeature(rootFeature)
    )

    override fun createDeepLink(deepLinkBuilder: DeepLinkConfig.DeepLinkBuilder): String =
        deepLinkBuilder.build()

    override fun cancelNotification(notificationId: Int) {
        try {
            NotificationManagerCompat.from(context).cancel(notificationId)
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Invalid notification ID: $notificationId")
        } catch (e: SecurityException) {
            Timber.e(e, "Permission denied for notification: $notificationId")
        }
    }

    override fun cancelAllNotifications() {
        try {
            NotificationManagerCompat.from(context).cancelAll()
        } catch (e: SecurityException) {
            Timber.e(e, "Permission denied for canceling all notifications")
        }
    }

    override fun createNotificationChannel(
        channelId: String,
        channelName: String,
        channelDescription: String,
        importance: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val androidImportance = when (importance) {
                NotificationService.NotificationImportance.MIN -> NotificationManager.IMPORTANCE_MIN
                NotificationService.NotificationImportance.LOW -> NotificationManager.IMPORTANCE_LOW
                NotificationService.NotificationImportance.DEFAULT -> NotificationManager.IMPORTANCE_DEFAULT
                NotificationService.NotificationImportance.HIGH -> NotificationManager.IMPORTANCE_HIGH
                NotificationService.NotificationImportance.MAX -> NotificationManager.IMPORTANCE_MAX
                else -> NotificationManager.IMPORTANCE_DEFAULT
            }

            val channel = NotificationChannel(channelId, channelName, androidImportance).apply {
                description = channelDescription
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun areNotificationsEnabled(): Boolean {
        return try {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } catch (e: SecurityException) {
            Timber.e(e, "Permission denied for notification check")
            false
        } catch (e: IllegalStateException) {
            Timber.e(e, "Invalid state for notification check")
            false
        }
    }

    override fun generateNotificationId(): Int = notificationIdCounter.getAndIncrement()

    private fun getChannelIdForScreenType(screenType: DeepLinkConfig.ScreenType): String =
        when (screenType) {
            DeepLinkConfig.ScreenType.DETAILS -> NotificationService.NotificationChannels.DETAILS
            DeepLinkConfig.ScreenType.PROFILE -> NotificationService.NotificationChannels.SOCIAL
            DeepLinkConfig.ScreenType.QUESTIONS -> NotificationService.NotificationChannels.NEWS
            DeepLinkConfig.ScreenType.HOME -> NotificationService.NotificationChannels.NEWS
            DeepLinkConfig.ScreenType.SETTINGS -> NotificationService.NotificationChannels.UPDATES
        }

    /**
     * Создает каналы уведомлений по умолчанию.
     */
    private fun createDefaultChannels() {
        createNotificationChannel(
            channelId = NotificationService.NotificationChannels.DETAILS,
            channelName = "Уведомления о деталях",
            channelDescription = "Уведомления для перехода к экрану деталей",
            importance = NotificationService.NotificationImportance.DEFAULT
        )

        createNotificationChannel(
            channelId = NotificationService.NotificationChannels.NEWS,
            channelName = "Новости",
            channelDescription = "Уведомления о новостях и обновлениях",
            importance = NotificationService.NotificationImportance.DEFAULT
        )

        createNotificationChannel(
            channelId = NotificationService.NotificationChannels.UPDATES,
            channelName = "Обновления приложения",
            channelDescription = "Уведомления об обновлениях приложения",
            importance = NotificationService.NotificationImportance.LOW
        )

        createNotificationChannel(
            channelId = NotificationService.NotificationChannels.ALERTS,
            channelName = "Важные уведомления",
            channelDescription = "Критически важные уведомления",
            importance = NotificationService.NotificationImportance.HIGH
        )

        createNotificationChannel(
            channelId = NotificationService.NotificationChannels.REMINDERS,
            channelName = "Напоминания",
            channelDescription = "Напоминания о задачах и событиях",
            importance = NotificationService.NotificationImportance.DEFAULT
        )

        createNotificationChannel(
            channelId = NotificationService.NotificationChannels.SOCIAL,
            channelName = "Социальные уведомления",
            channelDescription = "Уведомления от друзей и профиля",
            importance = NotificationService.NotificationImportance.DEFAULT
        )
    }
} 