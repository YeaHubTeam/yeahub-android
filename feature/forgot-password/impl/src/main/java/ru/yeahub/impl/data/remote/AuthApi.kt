package ru.yeahub.impl.data.remote

import ru.yeahub.impl.data.dto.ForgotPasswordRequestDto
import ru.yeahub.impl.data.dto.ForgotPasswordResponseDto

interface AuthApi {
    suspend fun forgotPassword(request: ForgotPasswordRequestDto): ForgotPasswordResponseDto
}