package ru.yeahub.public_questions.impl.data.di

import org.koin.dsl.module
import ru.yeahub.public_questions.impl.data.mapper.DataToDomainMapper
import ru.yeahub.public_questions.impl.data.repository.QuestionsRepositoryImpl
import ru.yeahub.public_questions.impl.data.repository.remote.QuestionsRemoteDataSource
import ru.yeahub.public_questions.impl.data.repository.remote.QuestionsRemoteDataSourceApi
import ru.yeahub.public_questions.impl.domain.repository.QuestionsRepositoryApi

val questionRepositoryModule = module {
    single { DataToDomainMapper() }

    single<QuestionsRemoteDataSourceApi> {
        QuestionsRemoteDataSource(
            apiService = get()
        )
    }

    single<QuestionsRepositoryApi> {
        QuestionsRepositoryImpl(
            remoteDataSource = get(),
            mapper = get()
        )
    }
}