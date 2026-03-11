package ru.yeahub.authentication.impl.registration.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.registration.data.mapper.RegistrationDomainToDataMapper

internal val registrationMapperModule = module {
    singleOf(::RegistrationDomainToDataMapper)
}