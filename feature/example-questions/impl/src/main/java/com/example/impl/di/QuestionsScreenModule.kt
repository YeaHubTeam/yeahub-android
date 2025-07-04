package com.example.impl.di

import com.example.api.QuestionsScreenApi
import com.example.impl.presentation.QuestionsScreenApiImpl
import org.koin.dsl.module

internal val questionsScreenModule = module {
    single<QuestionsScreenApi> { QuestionsScreenApiImpl() }
}
