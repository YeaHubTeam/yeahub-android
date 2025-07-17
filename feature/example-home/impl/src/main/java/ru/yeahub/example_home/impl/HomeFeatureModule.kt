package ru.yeahub.example_home.impl

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.example_home.api.HomeScreenApi
import ru.yeahub.navigation_api.FeatureApi
import timber.log.Timber

val homeFeatureModule = module {
    single<HomeScreenApi> {
        Timber.d("HomeFeatureModule single: Creating HomeScreenApiImpl")
        HomeScreenApiImpl()
    }
    single { 
        Timber.d("HomeFeatureModule single: Creating HomeFeatureImpl")
        HomeFeatureImpl(get()) 
    } bind FeatureApi::class
}