package ru.yeahub.authentication.impl.login.domain.repository

import ru.yeahub.authentication.impl.login.domain.entity.AuthResult
import ru.yeahub.authentication.impl.login.domain.entity.LoginModel
/**
 * Контракт репозитория авторизации:
 * - login - выполняет вход по email и паролю
 */
interface LoginRepositoryApi {

    suspend fun login(loginModel: LoginModel): AuthResult
}