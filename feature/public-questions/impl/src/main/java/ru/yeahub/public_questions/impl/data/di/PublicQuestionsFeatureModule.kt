package ru.yeahub.public_questions.impl.data.di

import org.koin.dsl.module

val questionsModule = module {
    includes(
        publicQuestionsRepositoryModule,
        publicQuestionsUseCase,
        publicQuestionsToUiMapperModule,
        publicQuestionsToDomainMapperModule,
        questionsViewModelModule
    )
}