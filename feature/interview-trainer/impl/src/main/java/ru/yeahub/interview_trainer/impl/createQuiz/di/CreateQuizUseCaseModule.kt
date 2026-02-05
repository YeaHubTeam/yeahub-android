package ru.yeahub.interview_trainer.impl.createQuiz.di

import org.koin.dsl.module
import ru.yeahub.interview_trainer.impl.createQuiz.domain.GetSpecializationsListUseCaseImpl

val createQuizUseCaseModule = module {
    factory { GetSpecializationsListUseCaseImpl(repository = get()) }
}