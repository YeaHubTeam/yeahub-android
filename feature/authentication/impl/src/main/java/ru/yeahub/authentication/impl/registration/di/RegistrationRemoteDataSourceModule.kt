package ru.yeahub.authentication.impl.registration.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.registration.data.repository.remote.RegistrationRemoteDataSourceApi
import ru.yeahub.authentication.impl.registration.data.repository.remote.RegistrationRemoteDataSourceImpl

internal val registrationRemoteDataSourceImplModule = module {
    singleOf(::RegistrationRemoteDataSourceImpl) { bind<RegistrationRemoteDataSourceApi>() }
}