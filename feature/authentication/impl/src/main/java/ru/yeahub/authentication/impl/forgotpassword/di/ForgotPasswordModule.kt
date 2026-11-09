package ru.yeahub.authentication.impl.forgotpassword.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.forgotpassword.presentation.ForgotPasswordViewModel

val forgotPasswordModule = module {
    viewModelOf(::ForgotPasswordViewModel)
}