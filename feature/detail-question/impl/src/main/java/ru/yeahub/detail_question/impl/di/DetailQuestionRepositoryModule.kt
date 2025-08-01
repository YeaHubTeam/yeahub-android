package ru.yeahub.detail_question.impl.di

import org.koin.dsl.module
import ru.yeahub.detail_question.impl.data.repository.DetailQuestionRepositoryImpl
import ru.yeahub.detail_question.impl.domain.repository.DetailQuestionRepository

internal val detailQuestionRepositoryModule = module {
    single<DetailQuestionRepository> {
        DetailQuestionRepositoryImpl(
            apiService = get(),
            dataToDomainMapper = get()
        )
    }
}