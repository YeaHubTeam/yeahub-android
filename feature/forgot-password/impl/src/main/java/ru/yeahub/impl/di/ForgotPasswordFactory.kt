package ru.yeahub.impl.di

import ru.yeahub.impl.data.mapper.ForgotPasswordMapper
import ru.yeahub.impl.data.remote.AuthApi
import ru.yeahub.impl.data.repository.ForgotPasswordRepositoryImpl
import ru.yeahub.impl.domain.SendResetLinkUseCase
import ru.yeahub.impl.presentation.ForgotPasswordViewModel
import ru.yeahub.impl.presentation.mapper.EmailValidator
import ru.yeahub.impl.presentation.mapper.ForgotPasswordScreenMapper

object ForgotPasswordFactory {

    fun createViewModel(
        api: AuthApi,
        mapper: ForgotPasswordMapper,
        emailValidator: EmailValidator,
    ): ForgotPasswordViewModel {
        val forgotPasswordRepository = ForgotPasswordRepositoryImpl(api, mapper)
        val sendResetLinkUseCase = SendResetLinkUseCase(forgotPasswordRepository)
        val forgotPasswordScreenMapper = ForgotPasswordScreenMapper(emailValidator)
        return ForgotPasswordViewModel(forgotPasswordScreenMapper, sendResetLinkUseCase)
    }
}