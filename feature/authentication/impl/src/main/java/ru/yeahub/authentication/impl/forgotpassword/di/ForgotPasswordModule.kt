package ru.yeahub.authentication.impl.forgotpassword.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.forgotpassword.data.repository.ForgotPasswordRepositoryImpl
import ru.yeahub.authentication.impl.forgotpassword.data.repository.remote.ForgotPasswordRemoteDataSourceApi
import ru.yeahub.authentication.impl.forgotpassword.data.repository.remote.ForgotPasswordRemoteDataSourceImpl
import ru.yeahub.authentication.impl.forgotpassword.domain.repository.ForgotPasswordRepositoryApi
import ru.yeahub.authentication.impl.forgotpassword.presentation.viewmodel.ForgotPasswordViewModel

val forgotPasswordModule = module {
    singleOf(::ForgotPasswordRemoteDataSourceImpl) {
        bind<ForgotPasswordRemoteDataSourceApi>()
    }

    singleOf(::ForgotPasswordRepositoryImpl) {
        bind<ForgotPasswordRepositoryApi>()
    }

    viewModelOf(::ForgotPasswordViewModel)
}