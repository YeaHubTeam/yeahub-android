package ru.yeahub.example_home.impl.data.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.example_home.impl.QuestionMainFeatureImpl
import ru.yeahub.navigation_api.FeatureApi

val questionsMainFeatureModule = module {
    includes(
        publicQuestionsToDomainMapperModule,
        questionsViewModelModule
    )
    single {
        QuestionMainFeatureImpl()
    } bind FeatureApi::class
}