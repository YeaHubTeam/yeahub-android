package ru.yeahub.authentication.impl.registration.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.registration.domain.usecase.RegistrationUseCase

internal val registrationUseCaseModule = module { factoryOf(::RegistrationUseCase) }
