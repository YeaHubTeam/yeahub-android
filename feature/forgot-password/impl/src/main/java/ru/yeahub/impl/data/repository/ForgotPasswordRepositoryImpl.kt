package ru.yeahub.impl.data.repository

import java.io.IOException
import timber.log.Timber
import ru.yeahub.impl.data.dto.ForgotPasswordRequestDto
import ru.yeahub.impl.data.mapper.ForgotPasswordMapper
import ru.yeahub.impl.data.remote.AuthApi
import ru.yeahub.impl.domain.ForgotPasswordRepository
import ru.yeahub.impl.domain.ForgotPasswordResult

class ForgotPasswordRepositoryImpl(
    private val api: AuthApi,
    private val mapper: ForgotPasswordMapper
) : ForgotPasswordRepository {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun sendResetLink(email: String): ForgotPasswordResult {
        return try {
            val dto = api.forgotPassword(ForgotPasswordRequestDto(email))
            mapper.toDomain(dto)
        } catch (e: IOException) {
            Timber.e("Network error sending reset link for email: $email, error: ${e.message}")
            ForgotPasswordResult.Error("Ошибка сети. Проверьте подключение к интернету.")
        } catch (e: IllegalArgumentException) {
            Timber.e("Invalid data error sending reset link for email: $email, error: ${e.message}")
            ForgotPasswordResult.Error("Некорректные данные. Проверьте введенный email.")
        } catch (e: Exception) {
            Timber.e("Unexpected error sending reset link for email: $email, error: ${e.message}")
            ForgotPasswordResult.Error("Неизвестная ошибка. Попробуйте позже.")
        }
    }
}