package com.example.impl.di

import com.example.impl.data.repository.DetailQuestionRepositoryImpl
import com.example.impl.domain.repository.DetailQuestionRepository
import org.koin.dsl.module


internal val detailQuestionRepositoryModule = module {
    single<DetailQuestionRepository> {
        DetailQuestionRepositoryImpl(
            apiService = get(),
            dataToDomainMapper = get()
        )
    }
}