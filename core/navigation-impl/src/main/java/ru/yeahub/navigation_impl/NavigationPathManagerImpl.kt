package ru.yeahub.navigation_impl

import ru.yeahub.navigation_api.NavigationPathManager
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

/**
 * Реализация менеджера путей навигации.
 * 
 * Потокобезопасная реализация для управления состоянием навигации
 * в многопоточной среде Android.
 */
class NavigationPathManagerImpl : NavigationPathManager {
    
    private var currentPath: String = ""
    private val featurePaths = ConcurrentHashMap<String, String>()
    private val pathHistory = mutableListOf<String>()
    
    companion object {
        private const val PATH_SEPARATOR = "/"
        private const val PARAMETER_PREFIX = "{"
        private const val PARAMETER_SUFFIX = "}"
        private const val MAX_HISTORY_SIZE = 20
    }
    
    override fun getCurrentPath(): String {
        Timber.d("NavigationPathManager getCurrentPath: $currentPath")
        return currentPath
    }
    
    override fun setCurrentPath(path: String) {
        Timber.d("NavigationPathManager setCurrentPath: $currentPath -> $path")
        
        // Добавляем в историю только если путь отличается
        if (currentPath != path) {
            pathHistory.add(currentPath)
            // Ограничиваем размер истории
            if (pathHistory.size > MAX_HISTORY_SIZE) {
                pathHistory.removeAt(0)
            }
        }
        
        currentPath = path
    }
    
    override fun createChildPath(featureName: String): String {
        val newPath = if (currentPath.isEmpty()) {
            featureName
        } else {
            "$currentPath$PATH_SEPARATOR$featureName"
        }
        
        Timber.d("NavigationPathManager createChildPath: $featureName -> $newPath")
        return newPath
    }
    
    override fun createParametrizedPath(featureName: String, vararg parameters: String): String {
        val basePath = createChildPath(featureName)
        
        return if (parameters.isEmpty()) {
            basePath
        } else {
            val paramPath = parameters.joinToString(PATH_SEPARATOR) { 
                "$PARAMETER_PREFIX$it$PARAMETER_SUFFIX" 
            }
            "$basePath$PATH_SEPARATOR$paramPath"
        }
    }
    
    override fun createConcretePath(parametrizedPath: String, vararg parameterValues: String): String {
        if (parameterValues.isEmpty()) {
            return parametrizedPath
        }
        
        var concretePath = parametrizedPath
        parameterValues.forEach { value ->
            // Заменяем первый встреченный параметр
            val parameterRegex = "\\{[^}]+\\}".toRegex()
            concretePath = parameterRegex.replaceFirst(concretePath, value)
        }
        
        Timber.d("NavigationPathManager createConcretePath: $parametrizedPath -> $concretePath")
        return concretePath
    }
    
    override fun getParentPath(): String {
        if (currentPath.isEmpty()) {
            return ""
        }
        
        val lastSeparatorIndex = currentPath.lastIndexOf(PATH_SEPARATOR)
        val parentPath = if (lastSeparatorIndex > 0) {
            currentPath.substring(0, lastSeparatorIndex)
        } else {
            ""
        }
        
        Timber.d("NavigationPathManager getParentPath: $currentPath -> $parentPath")
        return parentPath
    }
    
    override fun isRootPath(path: String): Boolean {
        return path.isEmpty() || !path.contains(PATH_SEPARATOR)
    }
    
    override fun registerFeaturePath(featureName: String, basePath: String) {
        Timber.d("NavigationPathManager registerFeaturePath: $featureName -> $basePath")
        featurePaths[featureName] = basePath
    }
    
    override fun getFeaturePath(featureName: String): String? {
        return featurePaths[featureName]
    }
    
    override fun clearPaths() {
        Timber.d("NavigationPathManager clearPaths: Clearing all paths")
        currentPath = ""
        featurePaths.clear()
        pathHistory.clear()
    }
    
    override fun createDirectPath(
        rootFeatureName: String,
        childFeatureName: String,
        vararg parameterValues: String
    ): String {
        val directPath = if (parameterValues.isEmpty()) {
            "$rootFeatureName$PATH_SEPARATOR$childFeatureName"
        } else {
            val paramPath = parameterValues.joinToString(PATH_SEPARATOR)
            "$rootFeatureName$PATH_SEPARATOR$childFeatureName$PATH_SEPARATOR$paramPath"
        }
        
        Timber.d(
            "NavigationPathManager createDirectPath: $rootFeatureName/$childFeatureName -> $directPath"
        )
        return directPath
    }
    
    override fun prepareForDirectNavigation(directPath: String) {
        Timber.d("NavigationPathManager prepareForDirectNavigation: $directPath")
        
        // Очищаем текущую историю, так как мы делаем прямой переход
        pathHistory.clear()
        
        // Создаем правильную иерархию путей для корректной навигации назад
        val pathParts = directPath.split(PATH_SEPARATOR)
        
        if (pathParts.size >= 2) {
            // Добавляем корневую фичу в историю
            val rootFeatureName = pathParts[0]
            pathHistory.add(rootFeatureName)
            
            // Устанавливаем полный путь как текущий
            currentPath = directPath
            
            Timber.d(
                "NavigationPathManager prepareForDirectNavigation: Current path set to: $currentPath"
            )
            Timber.d("NavigationPathManager prepareForDirectNavigation: History: $pathHistory")
        }
    }
    
    /**
     * Получает историю навигации.
     * 
     * @return Список предыдущих путей
     */
    fun getPathHistory(): List<String> {
        return pathHistory.toList()
    }
    
    /**
     * Проверяет, содержит ли путь параметры.
     * 
     * @param path Путь для проверки
     * @return true, если путь содержит параметры
     */
    fun hasParameters(path: String): Boolean {
        return path.contains(PARAMETER_PREFIX) && path.contains(PARAMETER_SUFFIX)
    }
} 