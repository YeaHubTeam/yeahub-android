package com.example.impl.di

import org.koin.dsl.module

val questionsFeatureModule = module {
    includes(
        questionsScreenModule,
        questionsRepositoryModule,
        questionsUseCaseModule,
        questionsViewModelModule
    )
}