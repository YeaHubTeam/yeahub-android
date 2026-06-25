package ru.yeahub.authentication.impl.login.domain.usecase

import ru.yeahub.authentication.impl.login.domain.repository.AuthSessionRepository

/**
 * UseCase проверки авторизованного состояния:
 * - проверяет наличие сохранённого access token
 */
class CheckAuthStateUseCase(
    private val authSessionRepository: AuthSessionRepository,
) {

    suspend operator fun invoke(): Boolean {
        return authSessionRepository.getAccessToken().isNullOrBlank().not()
    }
}