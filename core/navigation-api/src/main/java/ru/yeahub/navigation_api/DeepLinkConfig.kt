package ru.yeahub.navigation_api

import android.content.Intent
import android.net.Uri

/**
 * Конфигурация deep links для приложения.
 * 
 * Предоставляет гибкую систему для создания и обработки deep links
 * различных типов экранов.
 */
object DeepLinkConfig {
    
    /**
     * Основная схема приложения для deep links.
     */
    const val DEFAULT_SCHEME = "yeahub"
    
    /**
     * Типы поддерживаемых экранов для deep links.
     */
    enum class ScreenType(val host: String, val childFeatureName: String) {
        DETAILS("details", "details"),
        PROFILE("profile", "profile"),
        QUESTIONS("questions", "questions"),
        HOME("home", "home"),
        SETTINGS("settings", "settings");

        companion object {
            fun fromHost(host: String): ScreenType? {
                return values().find { it.host == host }
            }
        }
    }
    
    /**
     * Стандартные параметры для deep links.
     */
    object Parameters {
        const val ITEM_ID = "itemId"
        const val TITLE = "title"
        const val ROOT_FEATURE = "rootFeature"
        const val USER_ID = "userId"
        const val USER_NAME = "userName"
        const val CATEGORY = "category"
        const val FILTER = "filter"
        const val TAB = "tab"
    }
    
    /**
     * Значения по умолчанию для параметров.
     */
    object Defaults {
        const val ROOT_FEATURE = "home"
        const val TITLE = "Экран"
        const val CATEGORY = "general"
        const val TAB = "main"
    }
    
    /**
     * Билдер для создания deep links.
     */
    class DeepLinkBuilder(
        private val scheme: String = DEFAULT_SCHEME,
        private val screenType: ScreenType
    ) {
        private val parameters = mutableMapOf<String, String>()
        
        fun addParameter(key: String, value: String): DeepLinkBuilder {
            parameters[key] = value
            return this
        }
        
        fun itemId(value: String): DeepLinkBuilder = addParameter(Parameters.ITEM_ID, value)
        fun title(value: String): DeepLinkBuilder = addParameter(Parameters.TITLE, value)
        fun rootFeature(value: String): DeepLinkBuilder = addParameter(Parameters.ROOT_FEATURE, value)
        fun userId(value: String): DeepLinkBuilder = addParameter(Parameters.USER_ID, value)
        fun userName(value: String): DeepLinkBuilder = addParameter(Parameters.USER_NAME, value)
        fun category(value: String): DeepLinkBuilder = addParameter(Parameters.CATEGORY, value)
        fun filter(value: String): DeepLinkBuilder = addParameter(Parameters.FILTER, value)
        fun tab(value: String): DeepLinkBuilder = addParameter(Parameters.TAB, value)
        
        fun build(): String {
            val uriBuilder = Uri.Builder()
                .scheme(scheme)
                .authority(screenType.host)
            
            parameters.forEach { (key, value) ->
                uriBuilder.appendQueryParameter(key, value)
            }
            
            return uriBuilder.build().toString()
        }
        
        fun buildUri(): Uri {
            return Uri.parse(build())
        }
        
        fun buildIntent(): Intent {
            val intent = Intent(Intent.ACTION_VIEW, buildUri())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }
    
    /**
     * Парсер для обработки deep links.
     */
    class DeepLinkParser(private val uri: Uri) {
        
        fun getScreenType(): ScreenType? {
            return uri.host?.let { ScreenType.fromHost(it) }
        }
        
        fun getParameter(key: String): String? {
            return uri.getQueryParameter(key)
        }
        
        fun getParameterOrDefault(key: String, defaultValue: String): String {
            return getParameter(key) ?: defaultValue
        }
        
        fun getItemId(): String? = getParameter(Parameters.ITEM_ID)
        fun getTitle(): String = getParameterOrDefault(Parameters.TITLE, Defaults.TITLE)
        fun getRootFeature(): String = getParameterOrDefault(Parameters.ROOT_FEATURE, Defaults.ROOT_FEATURE)
        fun getUserId(): String? = getParameter(Parameters.USER_ID)
        fun getUserName(): String? = getParameter(Parameters.USER_NAME)
        fun getCategory(): String = getParameterOrDefault(Parameters.CATEGORY, Defaults.CATEGORY)
        fun getFilter(): String? = getParameter(Parameters.FILTER)
        fun getTab(): String = getParameterOrDefault(Parameters.TAB, Defaults.TAB)
        
        fun getAllParameters(): Map<String, String> {
            val params = mutableMapOf<String, String>()
            uri.queryParameterNames.forEach { key ->
                uri.getQueryParameter(key)?.let { value ->
                    params[key] = value
                }
            }
            return params
        }
        
        fun isValidScheme(expectedScheme: String = DEFAULT_SCHEME): Boolean {
            return uri.scheme == expectedScheme
        }
        
        fun isValidDeepLink(): Boolean {
            return isValidScheme() && getScreenType() != null
        }
    }
    
    /**
     * Фабрика для создания билдеров deep links.
     */
    object Factory {
        fun details(): DeepLinkBuilder = DeepLinkBuilder(screenType = ScreenType.DETAILS)
        fun profile(): DeepLinkBuilder = DeepLinkBuilder(screenType = ScreenType.PROFILE)
        fun questions(): DeepLinkBuilder = DeepLinkBuilder(screenType = ScreenType.QUESTIONS)
        fun home(): DeepLinkBuilder = DeepLinkBuilder(screenType = ScreenType.HOME)
        fun settings(): DeepLinkBuilder = DeepLinkBuilder(screenType = ScreenType.SETTINGS)
        
        fun custom(screenType: ScreenType): DeepLinkBuilder = DeepLinkBuilder(screenType = screenType)
        
        fun withScheme(scheme: String, screenType: ScreenType): DeepLinkBuilder {
            return DeepLinkBuilder(scheme = scheme, screenType = screenType)
        }
    }
    
    /**
     * Утилиты для работы с deep links.
     */
    object Utils {
        @Suppress("TooGenericExceptionCaught", "SwallowedException")
        fun parseDeepLink(uriString: String): DeepLinkParser? {
            return try {
                val uri = Uri.parse(uriString)
                DeepLinkParser(uri)
            } catch (e: IllegalArgumentException) {
                // Неверный формат URI - это ожидаемое поведение для невалидных строк
                null
            } catch (e: NullPointerException) {
                // Null URI string - это ожидаемое поведение для null строк
                null
            } catch (e: SecurityException) {
                // Проблемы с безопасностью при парсинге URI - это ожидаемое поведение
                null
            }
        }
        
        fun parseDeepLink(uri: Uri): DeepLinkParser = DeepLinkParser(uri)
        
        fun parseDeepLink(intent: Intent): DeepLinkParser? {
            return intent.data?.let { DeepLinkParser(it) }
        }
        
        fun isValidDeepLink(uriString: String): Boolean {
            return parseDeepLink(uriString)?.isValidDeepLink() ?: false
        }
        
        fun isValidDeepLink(uri: Uri): Boolean {
            return DeepLinkParser(uri).isValidDeepLink()
        }
        
        fun isValidDeepLink(intent: Intent): Boolean {
            return parseDeepLink(intent)?.isValidDeepLink() ?: false
        }
    }
} 