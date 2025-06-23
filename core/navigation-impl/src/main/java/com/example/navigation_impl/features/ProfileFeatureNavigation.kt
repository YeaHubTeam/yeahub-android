package com.example.navigation_impl.features

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.navigation_api.FeatureNavigation
import ru.yeahub.example_profile.api.ExampleProfileApi

/**
 * Адаптер для фичи Profile
 * 
 * АРХИТЕКТУРА:
 * Этот класс является адаптером между навигацией и фичей Profile.
 * Он реализует FeatureNavigation и делегирует вызовы к ExampleProfileApi.
 * 
 * ПРИНЦИП РАБОТЫ:
 * 1. Реализует FeatureNavigation для интеграции с навигацией
 * 2. Делегирует вызовы к ExampleProfileApi
 * 3. Связывает навигацию с фичей Profile
 * 
 * ИСПОЛЬЗОВАНИЕ:
 * Этот класс автоматически создается в NavigationFactory.
 * Не создавайте экземпляры вручную.
 * 
 * ПРИМЕЧАНИЕ:
 * Этот адаптер позволяет фиче Profile быть независимой от навигации,
 * но при этом интегрироваться с общей системой навигации.
 */
class ProfileFeatureNavigation(
    private val profileApi: ExampleProfileApi
) : FeatureNavigation {
    
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        // Делегируем регистрацию экранов к фиче Profile
        profileApi.registerGraph(navGraphBuilder, navController)
    }
    
    override fun getBaseRoute(): String = profileApi.getBaseRoute()
} 