package ru.yeahub.authentication.impl.forgot_password.data.remote

import ru.yeahub.authentication.impl.forgot_password.data.dto.ForgotPasswordRequestDto
import ru.yeahub.authentication.impl.forgot_password.data.dto.ForgotPasswordResponseDto

interface AuthApi {
    suspend fun forgotPassword(request: ForgotPasswordRequestDto): ForgotPasswordResponseDto
}