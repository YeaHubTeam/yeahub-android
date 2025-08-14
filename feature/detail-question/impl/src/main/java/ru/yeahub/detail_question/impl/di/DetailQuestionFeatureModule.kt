package ru.yeahub.detail_question.impl.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.detail_question.impl.presentation.DetailQuestionFeatureImpl
import ru.yeahub.navigation_api.FeatureApi

val detailQuestionFeatureModule = module {
    includes(
        detailQuestionRepositoryModule,
        detailQuestionMapperModule,
        detailQuestionUseCaseModule,
        detailQuestionViewModelModule
    )

    single { DetailQuestionFeatureImpl() } bind FeatureApi::class
}