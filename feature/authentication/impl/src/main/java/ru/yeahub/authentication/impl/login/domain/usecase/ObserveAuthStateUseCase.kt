package ru.yeahub.authentication.impl.login.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.yeahub.authentication.impl.login.domain.repository.AuthSessionRepository

class ObserveAuthStateUseCase(
    private val authSessionRepository: AuthSessionRepository,
) {

    operator fun invoke(): Flow<Boolean> {
        return authSessionRepository.observeAuthState()
    }
}