package ru.yeahub.impl.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.yeahub.impl.presentation.ForgotPasswordViewModel
import ru.yeahub.impl.presentation.validator.EmailValidator
import ru.yeahub.impl.presentation.validator.EmailValidatorImpl
import ru.yeahub.impl.presentation.mapper.ForgotPasswordScreenMapper

internal val forgotPasswordViewModelModule = module {
    factory<EmailValidator> { EmailValidatorImpl() }
    factory { ForgotPasswordScreenMapper(emailValidator = get()) }

    viewModel {
        ForgotPasswordViewModel(
            forgotPasswordScreenMapper = get(),
            sendResetLinkUseCase = get()
        )
    }
}