package ru.yeahub.authentication.impl.login.domain.usecase

import ru.yeahub.datastore_api.TokenDataStore

/**
 * UseCase выхода из аккаунта:
 * - очищает сохранённые токены авторизации
 */
class LogoutUseCase(
    private val tokenDataStore: TokenDataStore,
) {

    suspend operator fun invoke() {
        tokenDataStore.clearTokens()
    }
}