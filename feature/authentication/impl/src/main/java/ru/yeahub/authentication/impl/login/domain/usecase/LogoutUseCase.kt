package ru.yeahub.authentication.impl.login.domain.usecase

import ru.yeahub.authentication.impl.login.domain.repository.AuthSessionRepository
import ru.yeahub.datastore_api.TokenStorageResult

/**
 * UseCase выхода из аккаунта:
 * - очищает сохранённые токены авторизации
 */
class LogoutUseCase(
    private val authSessionRepository: AuthSessionRepository,
) {

    suspend operator fun invoke(): TokenStorageResult {
        return authSessionRepository.clearTokens()
    }
}