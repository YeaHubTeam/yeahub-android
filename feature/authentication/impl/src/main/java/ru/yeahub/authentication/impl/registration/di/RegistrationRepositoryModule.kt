package ru.yeahub.authentication.impl.registration.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.registration.data.repository.RegistrationRepositoryImpl
import ru.yeahub.authentication.impl.registration.domain.repository.RegistrationRepositoryApi

internal val registrationRepositoryModule = module {
    singleOf(::RegistrationRepositoryImpl) { bind<RegistrationRepositoryApi>() }
}