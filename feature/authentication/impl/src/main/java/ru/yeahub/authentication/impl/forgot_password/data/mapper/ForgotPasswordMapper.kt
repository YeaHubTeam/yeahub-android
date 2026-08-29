package ru.yeahub.authentication.impl.forgot_password.data.mapper

import ru.yeahub.authentication.impl.forgot_password.data.dto.ForgotPasswordResponseDto
import ru.yeahub.authentication.impl.forgot_password.domain.ForgotPasswordResult

class ForgotPasswordMapper {
    fun toDomain(responseDto: ForgotPasswordResponseDto): ForgotPasswordResult {
        return if (responseDto.ok) {
            ForgotPasswordResult.Success
        } else {
            ForgotPasswordResult.Error(responseDto.message)
        }
    }
}