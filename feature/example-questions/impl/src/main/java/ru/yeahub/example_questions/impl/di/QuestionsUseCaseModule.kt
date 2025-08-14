package ru.yeahub.example_questions.impl.di

import org.koin.dsl.module
import ru.yeahub.example_questions.impl.domain.QuestionsUseCase
import ru.yeahub.example_questions.impl.domain.QuestionsUseCaseImpl

internal val questionsUseCaseModule = module {
    single<QuestionsUseCase> { QuestionsUseCaseImpl(repository = get()) }
}