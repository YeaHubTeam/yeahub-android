package ru.yeahub.authentication.impl.login.domain.usecase

import ru.yeahub.authentication.impl.login.domain.entity.LoginModel
import ru.yeahub.authentication.impl.login.domain.repository.LoginRepositoryApi
/**
 * UseCase авторизации:
 * - login — запускает вход через репозиторий
 */
class LoginUseCase(
    private val repository: LoginRepositoryApi
) {
    suspend operator fun invoke(loginModel: LoginModel) {
        repository.login(loginModel)
    }
}