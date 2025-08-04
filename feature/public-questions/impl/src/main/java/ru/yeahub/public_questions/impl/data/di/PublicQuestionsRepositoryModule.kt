package ru.yeahub.public_questions.impl.data.di

import org.koin.dsl.module
import ru.yeahub.public_questions.impl.data.repository.PublicQuestionsRepositoryImpl
import ru.yeahub.public_questions.impl.data.repository.remote.PublicQuestionsRemoteDataSource
import ru.yeahub.public_questions.impl.data.repository.remote.PublicQuestionsRemoteDataSourceApi
import ru.yeahub.public_questions.impl.domain.repository.PublicQuestionsRepositoryApi

val publicQuestionsRepositoryModule = module {

    single<PublicQuestionsRemoteDataSourceApi> {
        PublicQuestionsRemoteDataSource(
            apiService = get()
        )
    }

    single<PublicQuestionsRepositoryApi> {
        PublicQuestionsRepositoryImpl(
            remoteDataSource = get(),
            mapper = get()
        )
    }
}