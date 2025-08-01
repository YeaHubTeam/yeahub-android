package ru.yeahub.navigation_api

import android.content.Intent

/**
 * Универсальный сервис для работы с уведомлениями.
 * 
 * Предоставляет единый интерфейс для создания уведомлений
 * с deep links и управления ими.
 */
interface NotificationService {
    
    /**
     * Показывает уведомление для перехода к экрану деталей.
     * 
     * @param itemId ID элемента для отображения
     * @param title Заголовок уведомления
     * @param message Текст уведомления
     * @param rootFeature Корневая фича для навигации
     * @param notificationId ID уведомления (по умолчанию генерируется автоматически)
     * @return true если уведомление было показано успешно
     */
    fun showDetailsNotification(
        itemId: String,
        title: String,
        message: String,
        rootFeature: String,
        notificationId: Int = generateNotificationId()
    ): Boolean
    
    /**
     * Показывает уведомление для любого типа экрана.
     * 
     * @param screenType Тип экрана для навигации
     * @param title Заголовок уведомления
     * @param message Текст уведомления
     * @param deepLinkBuilder Билдер для создания deep link
     * @param notificationId ID уведомления (по умолчанию генерируется автоматически)
     * @return true если уведомление было показано успешно
     */
    fun showNotification(
        screenType: DeepLinkConfig.ScreenType,
        title: String,
        message: String,
        deepLinkBuilder: DeepLinkConfig.DeepLinkBuilder,
        notificationId: Int = generateNotificationId()
    ): Boolean
    
    /**
     * Создает Intent для уведомления с deep link.
     * 
     * @param itemId ID элемента
     * @param title Заголовок элемента
     * @param rootFeature Корневая фича
     * @return Intent для уведомления
     */
    fun createNotificationIntent(
        itemId: String,
        title: String,
        rootFeature: String
    ): Intent
    
    /**
     * Создает Intent для любого типа экрана.
     * 
     * @param deepLinkBuilder Билдер для создания deep link
     * @return Intent для уведомления
     */
    fun createNotificationIntent(deepLinkBuilder: DeepLinkConfig.DeepLinkBuilder): Intent
    
    /**
     * Создает deep link для экрана деталей.
     * 
     * @param itemId ID элемента
     * @param title Заголовок элемента
     * @param rootFeature Корневая фича
     * @return Строка с deep link
     */
    fun createDetailsDeepLink(
        itemId: String,
        title: String,
        rootFeature: String
    ): String
    
    /**
     * Создает deep link для любого типа экрана.
     * 
     * @param deepLinkBuilder Билдер для создания deep link
     * @return Строка с deep link
     */
    fun createDeepLink(deepLinkBuilder: DeepLinkConfig.DeepLinkBuilder): String
    
    /**
     * Отменяет уведомление по ID.
     * 
     * @param notificationId ID уведомления для отмены
     */
    fun cancelNotification(notificationId: Int)
    
    /**
     * Отменяет все уведомления приложения.
     */
    fun cancelAllNotifications()
    
    /**
     * Создает канал уведомлений.
     * 
     * @param channelId ID канала
     * @param channelName Имя канала
     * @param channelDescription Описание канала
     * @param importance Важность уведомлений
     */
    fun createNotificationChannel(
        channelId: String,
        channelName: String,
        channelDescription: String,
        importance: Int
    )
    
    /**
     * Проверяет, разрешены ли уведомления.
     * 
     * @return true если уведомления разрешены
     */
    fun areNotificationsEnabled(): Boolean
    
    /**
     * Генерирует уникальный ID для уведомления.
     * 
     * @return Уникальный ID
     */
    fun generateNotificationId(): Int
    
    /**
     * Фабрика для создания билдеров deep links.
     */
    val deepLinkFactory: DeepLinkConfig.Factory
        get() = DeepLinkConfig.Factory
    
    /**
     * Утилиты для работы с deep links.
     */
    val deepLinkUtils: DeepLinkConfig.Utils
        get() = DeepLinkConfig.Utils
    
    /**
     * Константы для уровня важности уведомлений.
     */
    object NotificationImportance {
        const val MIN = 1
        const val LOW = 2
        const val DEFAULT = 3
        const val HIGH = 4
        const val MAX = 5
    }
    
    /**
     * Предопределенные каналы уведомлений.
     */
    object NotificationChannels {
        const val DETAILS = "details_channel"
        const val NEWS = "news_channel"
        const val UPDATES = "updates_channel"
        const val ALERTS = "alerts_channel"
        const val REMINDERS = "reminders_channel"
        const val SOCIAL = "social_channel"
    }
} 