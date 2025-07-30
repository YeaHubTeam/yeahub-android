package ru.yeahub.example_questions.impl.di

import org.koin.dsl.module
import ru.yeahub.example_questions.api.QuestionsRepository
import ru.yeahub.example_questions.impl.data.QuestionsRepositoryImpl

internal val questionsRepositoryModule = module {
    single<QuestionsRepository> { QuestionsRepositoryImpl(apiService = get()) }
}