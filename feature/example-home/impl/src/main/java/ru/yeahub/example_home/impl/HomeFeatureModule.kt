package ru.yeahub.example_home.impl

import android.util.Log
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.example_home.api.HomeFeatureApi
import ru.yeahub.example_home.api.HomeScreenApi
import ru.yeahub.navigation_api.FeatureApi

val homeFeatureModule = module {
    single<HomeScreenApi>  {
        Log.d("NavDebug", "Creating HomeScreenApiImpl")
        HomeScreenApiImpl()
    }
    single<HomeFeatureApi> { 
        Log.d("NavDebug", "Creating HomeFeatureImpl")
        HomeFeatureImpl(get()) 
    } bind FeatureApi::class
}