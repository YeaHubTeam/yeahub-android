package ru.yeahub.interview_trainer.impl.createQuiz.di

import org.koin.dsl.module
import ru.yeahub.interview_trainer.impl.createQuiz.data.CreateQuizRepositoryImpl
import ru.yeahub.interview_trainer.impl.createQuiz.domain.CreateQuizRepositoryApi

val createQuizRepositoryModule = module {
    single<CreateQuizRepositoryApi> {
        CreateQuizRepositoryImpl(
            apiService = get(),
            mapper = get()
        )
    }
}