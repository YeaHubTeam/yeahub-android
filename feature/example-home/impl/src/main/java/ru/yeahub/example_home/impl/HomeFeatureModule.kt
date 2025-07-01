package ru.yeahub.example_home.impl

import org.koin.dsl.module
import ru.yeahub.example_home.api.HomeScreenApi

val homeFeatureModule = module {
    single<HomeScreenApi> { HomeScreenApiImpl() }
} 