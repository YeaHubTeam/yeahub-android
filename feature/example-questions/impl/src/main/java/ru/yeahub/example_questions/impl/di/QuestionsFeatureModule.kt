package ru.yeahub.example_questions.impl.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.example_questions.impl.QuestionsFeatureImpl
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