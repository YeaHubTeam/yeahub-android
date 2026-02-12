package ru.yeahub.impl.data.repository

import ru.yeahub.impl.data.dto.ForgotPasswordRequestDto
import ru.yeahub.impl.data.remote.AuthApi
import ru.yeahub.impl.domain.ForgotPasswordRepository
import ru.yeahub.impl.domain.ForgotPasswordResult

class ForgotPasswordRepositoryImpl(
    private val api: AuthApi
) : ForgotPasswordRepository {
    override suspend fun sendResetLink(email: String): ForgotPasswordResult {
        return try {
            val dto = api.forgotPassword(ForgotPasswordRequestDto(email))
            dto.toDomain()
        } catch (t: Throwable) {
            // Здесь можно разрулить HttpException, IOException и т.п.
            ForgotPasswordResult.Error("Ошибка сети. Попробуйте ещё раз.")
        }
    }
}