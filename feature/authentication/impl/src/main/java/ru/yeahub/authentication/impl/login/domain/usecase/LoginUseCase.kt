package ru.yeahub.authentication.impl.login.domain.usecase

import ru.yeahub.authentication.impl.login.domain.entity.AuthResult
import ru.yeahub.authentication.impl.login.domain.entity.Failure
import ru.yeahub.authentication.impl.login.domain.entity.LoginError
import ru.yeahub.authentication.impl.login.domain.entity.LoginException
import ru.yeahub.authentication.impl.login.domain.entity.LoginModel
import ru.yeahub.authentication.impl.login.domain.repository.AuthSessionRepository
import ru.yeahub.authentication.impl.login.domain.repository.LoginRepositoryApi
import ru.yeahub.datastore_api.TokenStorageResult

/**
 * UseCase авторизации:
 * - выполняет login через LoginRepository
 * - сохраняет access token через AuthSessionRepository
 * - возвращает результат успешной авторизации
 * - маппит ошибку локального хранения в LoginError.TokenSaveFailed
 */
class LoginUseCase(
    private val loginRepository: LoginRepositoryApi,
    private val authSessionRepository: AuthSessionRepository,
) {

    suspend operator fun invoke(loginModel: LoginModel): AuthResult {
        val authResult = loginRepository.login(loginModel)

        return when (
            authSessionRepository.saveAccessToken(
                accessToken = authResult.tokens.accessToken,
            )
        ) {
            TokenStorageResult.Success -> authResult

            TokenStorageResult.Error -> throw LoginException(
                error = LoginError.TokenSaveFailed,
                failure = Failure(),
            )
        }
    }
}