package ru.yeahub.authentication.impl.forgot_password.data.repository

import kotlinx.coroutines.CancellationException
import ru.yeahub.authentication.impl.forgot_password.data.dto.ForgotPasswordRequestDto
import ru.yeahub.authentication.impl.forgot_password.data.mapper.ForgotPasswordMapper
import ru.yeahub.authentication.impl.forgot_password.data.remote.AuthApi
import ru.yeahub.authentication.impl.forgot_password.domain.ForgotPasswordRepository
import ru.yeahub.authentication.impl.forgot_password.domain.ForgotPasswordResult
import java.io.IOException
import timber.log.Timber

class ForgotPasswordRepositoryImpl(
    private val api: AuthApi,
    private val mapper: ForgotPasswordMapper
) : ForgotPasswordRepository {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun sendResetLink(email: String): ForgotPasswordResult {
        return try {
            val dto = api.forgotPassword(ForgotPasswordRequestDto(email))
            mapper.toDomain(dto)
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Timber.e(
                "Network error sending reset link for email: $email," +
                        "error: ${e.message}"
            )
            ForgotPasswordResult.Error(
                "Ошибка сети. Проверьте подключение к интернету."
            )
        } catch (e: IllegalArgumentException) {
            Timber.e(
                "Invalid data error sending reset link for email: $email, " +
                        "error: ${e.message}"
            )
            ForgotPasswordResult.Error(
                "Некорректные данные. Проверьте введенный email."
            )
        } catch (e: Exception) {
            Timber.e(
                "Unexpected error sending reset link for email: $email," +
                        "error: ${e.message}"
            )
            ForgotPasswordResult.Error("Неизвестная ошибка. Попробуйте позже.")
        }
    }
}