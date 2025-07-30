package ru.yeahub.detail_question.impl.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import ru.yeahub.detail_question.impl.domain.usecase.GetQuestionByIdUseCase
import ru.yeahub.detail_question.impl.domain.usecase.GetQuestionByIdUseCaseImpl

internal val detailQuestionUseCaseModule = module {
    single<GetQuestionByIdUseCase> {
        GetQuestionByIdUseCaseImpl(
            detailQuestionRepository = get(),
            dispatcher = get()
        )
    }
    single<CoroutineDispatcher> { Dispatchers.IO }
}