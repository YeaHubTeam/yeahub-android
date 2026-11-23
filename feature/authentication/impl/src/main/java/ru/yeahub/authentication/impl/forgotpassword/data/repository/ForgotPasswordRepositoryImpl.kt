package ru.yeahub.authentication.impl.forgotpassword.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import ru.yeahub.authentication.impl.forgotpassword.data.repository.remote.ForgotPasswordRemoteDataSourceApi
import ru.yeahub.authentication.impl.forgotpassword.domain.model.ForgotPasswordException
import ru.yeahub.authentication.impl.forgotpassword.domain.repository.ForgotPasswordRepositoryApi
import ru.yeahub.network_api.models.ForgotPasswordErrorDto
import java.io.IOException

class ForgotPasswordRepositoryImpl(
    private val forgotPasswordApi: ForgotPasswordRemoteDataSourceApi,
    private val gson: Gson
) : ForgotPasswordRepositoryApi {
    override suspend fun resetPassword(email: String): String {
        return try {
            forgotPasswordApi.resetPassword(email).message
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            val errorBody = e.response()
                ?.errorBody()
                ?.string()

            val errorDto = errorBody?.let {
                runCatching {
                    gson.fromJson(it, ForgotPasswordErrorDto::class.java)
                }.getOrNull()
            }

            throw ForgotPasswordException(
                httpCode = e.code(),
                serverMessage = errorDto?.description
                    ?: errorDto?.message,
                cause = e
            )
        } catch (e: IOException) {
            throw ForgotPasswordException(cause = e)
        }
    }
}