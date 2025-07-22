package com.example.impl.di

import com.example.impl.domain.usecase.GetQuestionByIdUseCase
import com.example.impl.domain.usecase.GetQuestionByIdUseCaseImpl
import org.koin.dsl.module

internal val detailQuestionUseCaseModule = module {
    single<GetQuestionByIdUseCase> {
        GetQuestionByIdUseCaseImpl(
            detailQuestionRepository = get()
        )
    }
}