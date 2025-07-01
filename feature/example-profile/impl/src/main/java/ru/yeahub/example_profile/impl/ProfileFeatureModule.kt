package ru.yeahub.example_profile.impl

import org.koin.dsl.module
import ru.yeahub.example_profile.api.ProfileScreenApi

val profileFeatureModule = module {
    single<ProfileScreenApi> { ProfileScreenApiImpl() }
} 