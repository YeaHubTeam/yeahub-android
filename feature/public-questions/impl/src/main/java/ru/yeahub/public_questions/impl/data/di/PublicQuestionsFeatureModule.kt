package ru.yeahub.public_questions.impl.data.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.public_questions.impl.PublicQuestionsFeatureImpl

val questionsModule = module {
    includes(
        publicQuestionsRepositoryModule,
        publicQuestionsUseCase,
        publicQuestionsToUiMapperModule,
        publicQuestionsToDomainMapperModule,
        questionsViewModelModule
    )
    single { PublicQuestionsFeatureImpl() } bind FeatureApi::class
}