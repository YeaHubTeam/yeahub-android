package ru.yeahub.example_details.impl

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.example_details.api.DetailsScreenApi
import ru.yeahub.navigation_api.FeatureApi

/**
 * Модуль DI для фичи Details.
 * 
 * Регистрирует:
 * - API экрана деталей
 * - Реализацию фичи
 */
val detailsFeatureModule = module {
    single<DetailsScreenApi> { DetailsScreenApiImpl() }
    single { DetailsFeatureImpl(get()) } bind FeatureApi::class
} 