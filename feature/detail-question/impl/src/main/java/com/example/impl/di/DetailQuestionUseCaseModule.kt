package com.example.impl.di

import com.example.impl.domain.usecase.GetQuestionByIdUseCase
import com.example.impl.domain.usecase.GetQuestionByIdUseCaseImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

internal val detailQuestionUseCaseModule = module {
    single<GetQuestionByIdUseCase> {
        GetQuestionByIdUseCaseImpl(
            detailQuestionRepository = get(),
            dispatcher = get()
        )
    }
    single<CoroutineDispatcher> { Dispatchers.IO }
}