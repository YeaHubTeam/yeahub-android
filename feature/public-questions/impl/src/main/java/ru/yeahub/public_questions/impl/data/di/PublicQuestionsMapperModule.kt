package ru.yeahub.public_questions.impl.data.di

import org.koin.dsl.module
import ru.yeahub.public_questions.impl.data.mapper.PublicQuestionsDataToDomainMapper
import ru.yeahub.public_questions.impl.presentation.mapper.PublicQuestionDomainToPresentationMapper

val publicQuestionsToUiMapperModule = module {
    single { PublicQuestionDomainToPresentationMapper() }
}
val publicQuestionsToDomainMapperModule = module {
    factory { PublicQuestionsDataToDomainMapper() }
}