package com.example.impl.di

import com.example.impl.domain.QuestionsUseCase
import com.example.impl.domain.QuestionsUseCaseImpl
import org.koin.dsl.module

internal val questionsUseCaseModule = module {
    single<QuestionsUseCase> { QuestionsUseCaseImpl(repository = get()) }
}