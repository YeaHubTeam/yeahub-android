package com.example.navigation_impl

import com.example.navigation_api.NavigationApi
import com.example.navigation_impl.features.HomeFeatureNavigation
import com.example.navigation_impl.features.ProfileFeatureNavigation
import ru.yeahub.example_profile.impl.ExampleProfileImpl

/**
 * Фабрика для создания навигации
 * 
 * КАК ДОБАВИТЬ НОВУЮ ФИЧУ:
 * 1. Создайте API и impl модули для фичи
 * 2. Создайте адаптер в navigation-impl (если нужно)
 * 3. Добавьте экземпляр фичи в список featureNavigations ниже
 * 
 * Пример для модульной фичи:
 * ```kotlin
 * // В API модуле
 * interface NewFeatureApi {
 *     fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController)
 *     fun getBaseRoute(): String
 * }
 * 
 * // В impl модуле
 * class NewFeatureImpl : NewFeatureApi { ... }
 * 
 * // В navigation-impl (адаптер)
 * class NewFeatureNavigation(private val api: NewFeatureApi) : FeatureNavigation {
 *     override fun registerGraph(...) = api.registerGraph(...)
 *     override fun getBaseRoute() = api.getBaseRoute()
 * }
 * 
 * // В NavigationFactory
 * NewFeatureNavigation(NewFeatureImpl())
 * ```
 */
object NavigationFactory {
    
    /**
     * Создает экземпляр NavigationApi с зарегистрированными фичами
     * 
     * ДЛЯ ДОБАВЛЕНИЯ НОВОЙ ФИЧИ:
     * Добавьте экземпляр вашей фичи в список featureNavigations
     */
    fun createNavigation(): NavigationApi {
        val featureNavigations = listOf<com.example.navigation_api.FeatureNavigation>(
            // TODO: Добавьте ваши фичи здесь
            // Пример: NewFeatureNavigation(NewFeatureImpl())
            HomeFeatureNavigation(),
            ProfileFeatureNavigation(ExampleProfileImpl())
        )
        
        return NavigationImpl(featureNavigations)
    }
    
    /**
     * Возвращает список всех зарегистрированных фич
     * 
     * ДЛЯ ДОБАВЛЕНИЯ НОВОЙ ФИЧИ:
     * Добавьте экземпляр вашей фичи в список
     */
    fun getFeatureNavigations(): List<com.example.navigation_api.FeatureNavigation> {
        return listOf<com.example.navigation_api.FeatureNavigation>(
            // TODO: Добавьте ваши фичи здесь
            // Пример: NewFeatureNavigation(NewFeatureImpl())
            HomeFeatureNavigation(),
            ProfileFeatureNavigation(ExampleProfileImpl())
        )
    }
} 