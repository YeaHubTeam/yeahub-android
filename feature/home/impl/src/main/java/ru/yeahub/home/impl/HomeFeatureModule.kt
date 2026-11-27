package ru.yeahub.home.impl

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.navigation_api.FeatureApi

val homeFeatureModule = module {
    single { HomeFeature() } bind FeatureApi::class
}
