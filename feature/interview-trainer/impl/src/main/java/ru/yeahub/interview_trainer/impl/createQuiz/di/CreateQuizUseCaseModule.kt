package ru.yeahub.interview_trainer.impl.createQuiz.di

import org.koin.dsl.module
import ru.yeahub.interview_trainer.impl.createQuiz.domain.GetSpecializationsListUseCaseImpl
import ru.yeahub.interview_trainer.impl.createQuiz.domain.GetSpecializationsUseCase

val createQuizUseCaseModule = module {
    factory<GetSpecializationsUseCase> {
        GetSpecializationsListUseCaseImpl(
            repository = get()
        )
    }
}