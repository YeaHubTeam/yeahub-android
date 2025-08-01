package ru.yeahub.example_questions.impl.di

import org.koin.dsl.module
import ru.yeahub.example_questions.api.QuestionsScreenApi
import ru.yeahub.example_questions.impl.presentation.QuestionsScreenApiImpl

internal val questionsScreenModule = module {
    single<QuestionsScreenApi> { QuestionsScreenApiImpl() }
}
