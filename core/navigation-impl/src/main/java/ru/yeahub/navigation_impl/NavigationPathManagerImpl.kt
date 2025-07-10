package ru.yeahub.navigation_impl

import ru.yeahub.navigation_api.NavigationPathManager
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
    
    override fun getCurrentPath(): String = currentPath
    
    override fun setCurrentPath(path: String) {
        if (currentPath != path) {
            pathHistory.add(currentPath)
            if (pathHistory.size > MAX_HISTORY_SIZE) {
                pathHistory.removeAt(0)
            }
        }
        currentPath = path
    }
    
    override fun createChildPath(featureName: String): String =
        if (currentPath.isEmpty()) featureName else "$currentPath$PATH_SEPARATOR$featureName"
    
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
        if (parameterValues.isEmpty()) return parametrizedPath
        
        var concretePath = parametrizedPath
        parameterValues.forEach { value ->
            val parameterRegex = "\\{[^}]+\\}".toRegex()
            concretePath = parameterRegex.replaceFirst(concretePath, value)
        }
        return concretePath
    }
    
    override fun getParentPath(): String {
        if (currentPath.isEmpty()) return ""
        
        val lastSeparatorIndex = currentPath.lastIndexOf(PATH_SEPARATOR)
        return if (lastSeparatorIndex > 0) {
            currentPath.substring(0, lastSeparatorIndex)
        } else {
            ""
        }
    }
    
    override fun isRootPath(path: String): Boolean = path.isEmpty() || !path.contains(PATH_SEPARATOR)
    
    override fun registerFeaturePath(featureName: String, basePath: String) {
        featurePaths[featureName] = basePath
    }
    
    override fun getFeaturePath(featureName: String): String? = featurePaths[featureName]
    
    override fun clearPaths() {
        currentPath = ""
        featurePaths.clear()
        pathHistory.clear()
    }
    
    override fun createDirectPath(
        rootFeatureName: String,
        childFeatureName: String,
        vararg parameterValues: String
    ): String {
        return if (parameterValues.isEmpty()) {
            "$rootFeatureName$PATH_SEPARATOR$childFeatureName"
        } else {
            val paramPath = parameterValues.joinToString(PATH_SEPARATOR)
            "$rootFeatureName$PATH_SEPARATOR$childFeatureName$PATH_SEPARATOR$paramPath"
        }
    }
    
    override fun prepareForDirectNavigation(directPath: String) {
        pathHistory.clear()
        
        val pathParts = directPath.split(PATH_SEPARATOR)
        if (pathParts.size >= 2) {
            val rootFeatureName = pathParts[0]
            pathHistory.add(rootFeatureName)
            currentPath = directPath
        }
    }
} 