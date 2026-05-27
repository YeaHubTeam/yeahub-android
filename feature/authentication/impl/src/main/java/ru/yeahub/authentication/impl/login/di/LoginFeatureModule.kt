package ru.yeahub.authentication.impl.login.di

import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.login.data.mapper.LoginDomainToDataMapper
import ru.yeahub.authentication.impl.login.data.mapper.LoginResponseToDomainMapper
import ru.yeahub.authentication.impl.login.data.repository.remote.LoginRemoteDataSourceApi
import ru.yeahub.authentication.impl.login.data.repository.LoginRepositoryImpl
import ru.yeahub.authentication.impl.login.data.repository.remote.LoginRemoteDataSourceImpl
import ru.yeahub.authentication.impl.login.domain.repository.LoginRepositoryApi
import ru.yeahub.authentication.impl.login.domain.usecase.LoginUseCase
import ru.yeahub.authentication.impl.login.presentation.mapper.LoginStateMapper
import ru.yeahub.authentication.impl.login.presentation.viewmodel.LoginViewModel

/**
 * DI модуль авторизации:
 * - регистрирует mapper'ы
 * - регистрирует remote data source
 * - регистрирует repository
 * - регистрирует use case
 * - регистрирует ViewModel
 */
val loginFeatureModule = module {
    singleOf(::LoginDomainToDataMapper)
    singleOf(::LoginResponseToDomainMapper)

    singleOf(::Gson)

    singleOf(::LoginRemoteDataSourceImpl) {
        bind<LoginRemoteDataSourceApi>()
    }

    singleOf(::LoginRepositoryImpl) {
        bind<LoginRepositoryApi>()
    }

    factoryOf(::LoginUseCase)

    singleOf(::LoginStateMapper)
    viewModelOf(::LoginViewModel)
}