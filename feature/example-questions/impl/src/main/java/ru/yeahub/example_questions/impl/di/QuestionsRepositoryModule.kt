package ru.yeahub.example_questions.impl.di

import org.koin.dsl.module
import ru.yeahub.example_questions.impl.data.QuestionsRepositoryImpl
import ru.yeahub.example_questions.api.QuestionsRepository

internal val questionsRepositoryModule = module {
    single<QuestionsRepository> { QuestionsRepositoryImpl(apiService = get()) }
}