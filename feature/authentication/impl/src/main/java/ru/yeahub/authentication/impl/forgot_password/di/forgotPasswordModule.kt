package ru.yeahub.authentication.impl.forgot_password.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.forgot_password.presentation.viewmodel.ForgotPasswordViewModel

val forgotPasswordModule = module {
    viewModelOf(::ForgotPasswordViewModel)
}