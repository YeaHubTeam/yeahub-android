package ru.yeahub.authentication.impl.registration.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.registration.presentation.RegistrationViewModel

internal val registrationViewModelModule = module { viewModelOf(::RegistrationViewModel) }
