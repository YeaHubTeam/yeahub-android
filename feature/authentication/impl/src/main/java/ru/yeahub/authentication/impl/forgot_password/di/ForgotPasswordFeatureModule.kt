package ru.yeahub.authentication.impl.forgot_password.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.yeahub.authentication.impl.forgot_password.data.mapper.ForgotPasswordMapper
import ru.yeahub.authentication.impl.forgot_password.data.repository.ForgotPasswordRepositoryImpl
import ru.yeahub.authentication.impl.forgot_password.domain.ChangePasswordUseCase
import ru.yeahub.authentication.impl.forgot_password.domain.ForgotPasswordRepository
import ru.yeahub.authentication.impl.forgot_password.domain.SendResetLinkUseCase
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.ChangePasswordViewModel
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.mapper.ChangePasswordStateMapper
import ru.yeahub.authentication.impl.forgot_password.presentation.email.ForgotPasswordEmailViewModel
import ru.yeahub.authentication.impl.forgot_password.presentation.email.mapper.ForgotPasswordEmailStateMapper

val forgotPasswordFeatureModule = module {
    singleOf(::ForgotPasswordMapper)

    singleOf(::ForgotPasswordRepositoryImpl) {
        bind<ForgotPasswordRepository>()
    }

    factoryOf(::SendResetLinkUseCase)
    factoryOf(::ChangePasswordUseCase)

    singleOf(::ForgotPasswordEmailStateMapper)
    singleOf(::ChangePasswordStateMapper)

    viewModelOf(::ForgotPasswordEmailViewModel)

    viewModel { parameters ->
        ChangePasswordViewModel(
            resetToken = parameters.get(),
            changePasswordUseCase = get(),
            mapper = get(),
        )
    }
}