package com.example.impl.di

import com.example.impl.data.QuestionsRepositoryImpl
import com.example.impl.domain.QuestionsRepository
import org.koin.dsl.module

internal val questionsRepositoryModule = module {
    single<QuestionsRepository> { QuestionsRepositoryImpl(apiService = get()) }
}