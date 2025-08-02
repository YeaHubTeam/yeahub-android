package ru.yeahub.public_questions.impl.data.di

import org.koin.dsl.module
import ru.yeahub.public_questions.impl.domain.usecase.GetQuestionUseCase

val useCase = module {
    factory { GetQuestionUseCase(get()) }
}