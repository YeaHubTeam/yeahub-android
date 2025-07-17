package com.example.impl.di

import com.example.impl.QuestionsFeatureImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.navigation_api.FeatureApi

val questionsFeatureModule = module {
    includes(
        questionsScreenModule,
        questionsRepositoryModule,
        questionsUseCaseModule,
        questionsViewModelModule
    )
    
    // Регистрируем QuestionsFeatureImpl как FeatureApi
    single { QuestionsFeatureImpl(get()) } bind FeatureApi::class
}