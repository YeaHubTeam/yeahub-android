package ru.yeahub.navigation_api

/**
 * Менеджер для динамического управления путями навигации.
 * 
 * Обеспечивает:
 * - Отслеживание текущих активных путей
 * - Создание новых маршрутов на основе текущего контекста
 * - Управление состоянием навигации
 */
interface NavigationPathManager {
    
    /**
     * Получает текущий активный путь навигации.
     * 
     * @return Текущий путь или пустая строка, если находимся в корне
     */
    fun getCurrentPath(): String
    
    /**
     * Устанавливает новый текущий путь.
     * 
     * @param path Новый текущий путь
     */
    fun setCurrentPath(path: String)
    
    /**
     * Создает новый путь на основе текущего пути и имени фичи.
     * 
     * @param featureName Имя фичи для добавления к пути
     * @return Новый полный путь
     */
    fun createChildPath(featureName: String): String
    
    /**
     * Создает путь с параметрами на основе текущего пути.
     * 
     * @param featureName Имя фичи
     * @param parameters Параметры для добавления к пути
     * @return Новый путь с параметрами
     */
    fun createParametrizedPath(featureName: String, vararg parameters: String): String
    
    /**
     * Создает конкретный путь с заданными значениями параметров.
     * 
     * @param parametrizedPath Путь с параметрами
     * @param parameterValues Значения параметров
     * @return Конкретный путь
     */
    fun createConcretePath(parametrizedPath: String, vararg parameterValues: String): String
    
    /**
     * Возвращается на один уровень назад в навигации.
     * 
     * @return Родительский путь
     */
    fun getParentPath(): String
    
    /**
     * Проверяет, является ли путь корневым.
     * 
     * @param path Путь для проверки
     * @return true, если путь корневой
     */
    fun isRootPath(path: String): Boolean
    
    /**
     * Регистрирует фичу с её базовым путем.
     * 
     * @param featureName Имя фичи
     * @param basePath Базовый путь фичи
     */
    fun registerFeaturePath(featureName: String, basePath: String)
    
    /**
     * Получает зарегистрированный путь для фичи.
     * 
     * @param featureName Имя фичи
     * @return Базовый путь фичи или null, если не зарегистрирована
     */
    fun getFeaturePath(featureName: String): String?
    
    /**
     * Очищает все зарегистрированные пути (для сброса состояния).
     */
    fun clearPaths()
    
    /**
     * Создает direct path для навигации из уведомлений.
     * Используется для прямого перехода к определенному экрану без учета текущего состояния навигации.
     * 
     * @param rootFeatureName Имя корневой фичи (например, "home", "stub")
     * @param childFeatureName Имя дочерней фичи (например, "details")
     * @param parameterValues Значения параметров для пути
     * @return Полный путь для навигации
     */
    fun createDirectPath(rootFeatureName: String, childFeatureName: String, vararg parameterValues: String): String
    
    /**
     * Подготавливает менеджер для direct navigation.
     * Устанавливает правильную иерархию путей для корректной навигации назад.
     * 
     * @param directPath Полный путь для direct navigation
     */
    fun prepareForDirectNavigation(directPath: String)
} 