package ru.yeahub.example_profile.impl

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.example_profile.api.ProfileFeatureApi
import ru.yeahub.example_profile.api.ProfileScreenApi
import ru.yeahub.navigation_api.FeatureApi

val profileFeatureModule = module {
    single<ProfileScreenApi> { ProfileScreenApiImpl() }
    single<ProfileFeatureApi> { ProfileFeatureImpl(get()) } bind FeatureApi::class
} 