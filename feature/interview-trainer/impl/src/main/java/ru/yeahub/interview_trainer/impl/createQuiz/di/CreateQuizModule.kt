package ru.yeahub.interview_trainer.impl.createQuiz.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.interview_trainer.impl.InterviewTrainerFeatureImpl
import ru.yeahub.navigation_api.FeatureApi

val createQuizModule = module {
    includes(
        createQuizScreenMapperModule,
        createQuizDataToDomainMapperModule,
        createQuizRepositoryModule,
        createQuizUseCaseModule,
        createQuizViewModelModule
    )
    single { InterviewTrainerFeatureImpl() } bind FeatureApi::class
}