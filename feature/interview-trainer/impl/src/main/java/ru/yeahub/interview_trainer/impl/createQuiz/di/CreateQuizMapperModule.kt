package ru.yeahub.interview_trainer.impl.createQuiz.di

import org.koin.dsl.module
import ru.yeahub.interview_trainer.impl.createQuiz.data.CreateQuizDataToDomainMapper
import ru.yeahub.interview_trainer.impl.createQuiz.presentation.CreateQuizScreenMapper

val createQuizScreenMapperModule = module {
    single { CreateQuizScreenMapper() }
}

val createQuizDataToDomainMapperModule = module {
    factory { CreateQuizDataToDomainMapper() }
}