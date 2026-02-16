package ru.yeahub.impl.di

import ru.yeahub.impl.data.mapper.ForgotPasswordMapper
import ru.yeahub.impl.data.remote.AuthApi
import ru.yeahub.impl.data.repository.ForgotPasswordRepositoryImpl
import ru.yeahub.impl.domain.SendResetLinkUseCase
import ru.yeahub.impl.presentation.ForgotPasswordViewModel

object ForgotPasswordFactory {
    fun createViewModel(api: AuthApi, mapper: ForgotPasswordMapper): ForgotPasswordViewModel {
        val repo = ForgotPasswordRepositoryImpl(api, mapper)
        val useCase = SendResetLinkUseCase(repo)
        return ForgotPasswordViewModel(useCase)
    }
}