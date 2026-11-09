package ru.yeahub.home.impl.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.home.impl.HomeFeatureImpl
import ru.yeahub.navigation_api.FeatureApi

val homeFeatureModule = module {
    includes(homeDataModule, homeUseCasesModule, homeViewModelsModule)

    single { HomeFeatureImpl() } bind FeatureApi::class
}
