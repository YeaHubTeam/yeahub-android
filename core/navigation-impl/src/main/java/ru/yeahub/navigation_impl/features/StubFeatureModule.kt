package ru.yeahub.navigation_impl.features

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.navigation_api.FeatureApi

/**
 * Модуль DI для stub фичи.
 *
 * Регистрирует stub фичу как FeatureApi для автоматической
 * регистрации в навигационной системе.
 */
val stubFeatureModule = module {
    single { StubFeatureImpl() } bind FeatureApi::class
} 