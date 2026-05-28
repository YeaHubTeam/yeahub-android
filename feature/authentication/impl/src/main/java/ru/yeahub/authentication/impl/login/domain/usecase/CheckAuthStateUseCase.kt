package ru.yeahub.authentication.impl.login.domain.usecase

import ru.yeahub.datastore_api.TokenDataStore

/**
 * UseCase проверки авторизации пользователя:
 * - возвращает true, если access token сохранён
 */
class CheckAuthStateUseCase(
    private val tokenDataStore: TokenDataStore,
) {

    suspend operator fun invoke(): Boolean {
        return tokenDataStore.getAccessToken().isNullOrBlank().not()
    }
}